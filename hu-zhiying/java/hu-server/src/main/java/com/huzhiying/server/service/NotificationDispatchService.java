package com.huzhiying.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.NotificationTaskStatus;
import com.huzhiying.server.dto.AdminOverviewDtos.NotificationDispatchResult;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.NotificationTaskEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.websocket.WebSocketEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NotificationDispatchService {

    private static final Logger log = LoggerFactory.getLogger(NotificationDispatchService.class);
    private static final DateTimeFormatter MESSAGE_TIME = DateTimeFormatter.ofPattern("MM-dd HH:mm");
    private static final String CHANNEL_PLATFORM = "PLATFORM";
    private static final String SYSTEM_SENDER = "system";

    private final PlatformRepository platformRepository;
    private final WebSocketEventGateway webSocketEventGateway;
    private final ObjectMapper objectMapper;

    public NotificationDispatchService(PlatformRepository platformRepository,
                                       WebSocketEventGateway webSocketEventGateway,
                                       ObjectMapper objectMapper) {
        this.platformRepository = platformRepository;
        this.webSocketEventGateway = webSocketEventGateway;
        this.objectMapper = objectMapper;
    }

    public NotificationDispatchResult dispatchPendingTasks(int limit) {
        int effectiveLimit = Math.max(limit, 1);
        List<NotificationTaskEntity> tasks = platformRepository.listPendingNotificationTasks(effectiveLimit);
        int successCount = 0;
        int failedCount = 0;

        for (NotificationTaskEntity task : tasks) {
            try {
                dispatch(task);
                markSent(task);
                successCount++;
            } catch (RuntimeException error) {
                markFailed(task);
                failedCount++;
                log.warn("notification_dispatch_failed taskId={} channel={} templateCode={}",
                        task.id, task.channel, task.templateCode, error);
            }
        }

        return new NotificationDispatchResult(effectiveLimit, tasks.size(), successCount, failedCount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dispatchPendingTasksToInbox(RoleCode roleCode, Long userId, int limit) {
        if (roleCode == null || userId == null) {
            return;
        }
        List<NotificationTaskEntity> tasks = platformRepository.listPendingNotificationTasksForTarget(roleCode.name(), userId, Math.max(limit, 1));
        for (NotificationTaskEntity task : tasks) {
            try {
                dispatch(task);
                markSent(task);
            } catch (RuntimeException error) {
                markFailed(task);
                log.warn("notification_inbox_dispatch_failed taskId={} role={} userId={}",
                        task.id, roleCode, userId, error);
            }
        }
    }

    private void dispatch(NotificationTaskEntity task) {
        if (!CHANNEL_PLATFORM.equalsIgnoreCase(task.channel)) {
            throw new IllegalStateException("unsupported notification channel: " + task.channel);
        }
        if (task.targetUserId != null && isInboxRole(task.targetRole)) {
            dispatchToSystemInbox(task);
            return;
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("taskId", task.id);
        payload.put("bizNo", task.bizNo);
        payload.put("bizType", task.bizType);
        payload.put("bizId", task.bizId);
        payload.put("targetRole", task.targetRole);
        payload.put("targetUserId", task.targetUserId);
        payload.put("templateCode", task.templateCode);
        payload.put("traceId", task.traceId);
        payload.put("payload", task.payloadText);
        webSocketEventGateway.publishNotificationTask(payload);
    }

    private void dispatchToSystemInbox(NotificationTaskEntity task) {
        String sessionId = systemSessionId(task.targetRole, task.targetUserId);
        MessageSessionEntity session = platformRepository.findMessageSession(sessionId).orElseGet(MessageSessionEntity::new);
        session.id = sessionId;
        session.orderId = null;
        session.title = "平台通知";
        session.participantUserId = task.targetUserId;
        platformRepository.saveMessageSession(session);

        MessageItemEntity item = new MessageItemEntity();
        item.sessionId = sessionId;
        item.senderCode = SYSTEM_SENDER;
        item.messageType = "text";
        item.contentText = renderMessage(task);
        item.messageTime = LocalDateTime.now().format(MESSAGE_TIME);
        MessageItemEntity saved = platformRepository.saveMessageItem(item);

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("id", saved.id);
        message.put("sessionId", saved.sessionId);
        message.put("sender", SYSTEM_SENDER);
        message.put("type", saved.messageType);
        message.put("content", saved.contentText);
        message.put("time", saved.messageTime);
        webSocketEventGateway.publishChatMessage(Map.of(
                "sessionId", sessionId,
                "orderId", "",
                "message", message
        ));
    }

    private String renderMessage(NotificationTaskEntity task) {
        String orderId = firstNonBlank(readPayloadValue(task.payloadText, "orderId"), task.bizId);
        return switch (normalizeTemplate(task.templateCode)) {
            case "payment-success" -> "订单 " + orderId + " 已支付成功，平台正在安排后续处理。";
            case "refund-completed" -> "订单 " + orderId + " 已完成退款，请留意原支付渠道到账。";
            case "settlement-approved" -> "订单 " + orderId + " 结算已通过审核，可在钱包查看入账。";
            case "refund-review-required" -> "订单 " + orderId + " 有新的退款申请，待后台审核。";
            default -> "订单 " + orderId + " 有新的平台通知，请打开查看详情。";
        };
    }

    private String readPayloadValue(String payloadText, String fieldName) {
        if (payloadText == null || payloadText.isBlank()) {
            return "";
        }
        try {
            JsonNode node = objectMapper.readTree(payloadText);
            return node.path(fieldName).asText("");
        } catch (Exception error) {
            return "";
        }
    }

    private boolean isInboxRole(String targetRole) {
        if (targetRole == null || targetRole.isBlank()) {
            return false;
        }
        String normalized = targetRole.trim().toUpperCase();
        return "USER".equals(normalized) || "MASTER".equals(normalized);
    }

    private String systemSessionId(String targetRole, Long targetUserId) {
        return "SYS-" + normalizeRole(targetRole) + "-" + targetUserId;
    }

    private String normalizeRole(String targetRole) {
        return targetRole == null || targetRole.isBlank() ? "USER" : targetRole.trim().toUpperCase();
    }

    private String normalizeTemplate(String templateCode) {
        return templateCode == null ? "" : templateCode.trim().toLowerCase();
    }

    private String firstNonBlank(String primary, String fallback) {
        if (primary != null && !primary.isBlank()) {
            return primary;
        }
        return fallback == null ? "" : fallback;
    }

    private void markSent(NotificationTaskEntity task) {
        LocalDateTime now = LocalDateTime.now();
        task.status = NotificationTaskStatus.SENT;
        task.sentAt = now;
        task.updatedAt = now;
        task.nextRetryAt = null;
        platformRepository.saveNotificationTask(task);
    }

    private void markFailed(NotificationTaskEntity task) {
        LocalDateTime now = LocalDateTime.now();
        task.status = NotificationTaskStatus.FAILED;
        task.updatedAt = now;
        task.nextRetryAt = now.plusMinutes(5);
        platformRepository.saveNotificationTask(task);
    }
}
