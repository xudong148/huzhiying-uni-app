package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.RefundRequestStatus;
import com.huzhiying.domain.enums.DomainEnums.SettlementBillStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.server.dto.AdminBusinessDtos;
import com.huzhiying.server.persistence.PersistenceEntities.AddressEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ArbitrationCaseEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CouponEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.OrderTrackPointEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationEntity;
import com.huzhiying.server.persistence.PersistenceEntities.RefundRequestEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.SettlementBillEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletTransactionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WorkStepRecordEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.service.payment.WechatPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台业务详情与动作服务。
 */
@Service
@Transactional(readOnly = true)
public class AdminBusinessService {

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final PlatformDomainSupport domainSupport;
    private final PlatformCommandService commandService;
    private final WechatPaymentService wechatPaymentService;

    public AdminBusinessService(PlatformRepository platformRepository,
                                PlatformAssembler assembler,
                                PlatformDomainSupport domainSupport,
                                PlatformCommandService commandService,
                                WechatPaymentService wechatPaymentService) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.domainSupport = domainSupport;
        this.commandService = commandService;
        this.wechatPaymentService = wechatPaymentService;
    }

    public AdminBusinessDtos.DispatchDetail dispatchDetail(String taskId) {
        DispatchTaskEntity task = platformRepository.findDispatchTask(taskId).orElseThrow();
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(task.orderId);
        AddressEntity address = platformRepository.findAddress(order.addressId).orElse(null);
        UserEntity masterUser = task.currentMasterUserId == null ? null : platformRepository.findUser(task.currentMasterUserId).orElse(null);

        return new AdminBusinessDtos.DispatchDetail(
                task.id,
                task.orderId,
                task.title,
                task.orderId != null && task.orderId.startsWith("PO") ? "PRODUCT" : "SERVICE",
                domainSupport.mapTaskStatus(task.taskStatus),
                task.dispatchMode == null ? "" : task.dispatchMode.name(),
                task.currentMasterUserId,
                masterUser == null ? "待分配" : masterUser.nickname,
                address == null ? "" : address.detailAddress,
                order.appointment,
                order.amount,
                task.areaText,
                assembler.splitList(task.tagsText),
                buildTimeline(order.id)
        );
    }

    @Transactional
    public AdminBusinessDtos.DispatchDetail assignTask(String taskId, AdminBusinessDtos.DispatchAssignRequest request) {
        String masterName = resolveMasterName(request.masterUserId(), request.masterName());
        commandService.forceAssignTask(taskId, masterName);
        return dispatchDetail(taskId);
    }

    @Transactional
    public AdminBusinessDtos.OrderDetail cancelDispatchOrder(String taskId, AdminBusinessDtos.ReasonRequest request) {
        DispatchTaskEntity task = platformRepository.findDispatchTask(taskId).orElseThrow();
        commandService.cancelOrder(task.orderId, request == null ? null : request.reason());
        return orderDetail(task.orderId);
    }

    public AdminBusinessDtos.OrderDetail orderDetail(String orderId) {
        return platformRepository.findServiceOrder(orderId)
                .map(this::buildServiceOrderDetail)
                .orElseGet(() -> buildProductOrderDetail(platformRepository.findProductOrder(orderId).orElseThrow()));
    }

    @Transactional
    public AdminBusinessDtos.OrderDetail cancelOrder(String orderId, AdminBusinessDtos.ReasonRequest request) {
        if (platformRepository.findProductOrder(orderId).isPresent()) {
            throw new IllegalStateException("商品订单暂不支持后台直接取消，请走退款流程。");
        }
        commandService.cancelOrder(orderId, request == null ? null : request.reason());
        return orderDetail(orderId);
    }

    @Transactional
    public AdminBusinessDtos.OrderDetail refundOrder(String orderId, AdminBusinessDtos.ReasonRequest request) {
        commandService.refundOrder(orderId, request == null ? null : request.reason());
        return orderDetail(orderId);
    }

    @Transactional
    public AdminBusinessDtos.OrderDetail grantCoupon(String orderId, AdminBusinessDtos.GrantCouponRequest request) {
        CouponEntity coupon = chooseCoupon(request);
        MessageSessionEntity session = findSessionByOrderId(orderId);
        if (session != null) {
            MessageItemEntity message = new MessageItemEntity();
            message.sessionId = session.id;
            message.senderCode = "customer_service";
            message.messageType = "text";
            message.contentText = "客服已补发优惠券：" + coupon.title + (request.remark() == null || request.remark().isBlank() ? "" : "，备注：" + request.remark());
            message.messageTime = "刚刚";
            platformRepository.saveMessageItem(message);
        }
        return orderDetail(orderId);
    }

    @Transactional
    public AdminBusinessDtos.OrderDetail updateAppointment(String orderId, AdminBusinessDtos.UpdateAppointmentRequest request) {
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(orderId);
        order.appointment = request.appointment();
        order.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(order);
        domainSupport.saveStep(orderId, "appointment_update", "改预约", "后台已调整预约时间为 " + request.appointment(), true);
        return orderDetail(orderId);
    }

    public AdminBusinessDtos.MasterDetail masterDetail(Long userId) {
        MasterProfileEntity profile = platformRepository.findMasterProfileByUserId(userId).orElseThrow();
        UserEntity user = platformRepository.findUser(userId).orElseThrow();
        return toMasterDetail(profile, user);
    }

    @Transactional
    public AdminBusinessDtos.MasterDetail updateMaster(Long userId, AdminBusinessDtos.MasterUpdateRequest request) {
        MasterProfileEntity profile = platformRepository.findMasterProfileByUserId(userId).orElseThrow();
        UserEntity user = platformRepository.findUser(userId).orElseThrow();

        if (request.realName() != null && !request.realName().isBlank()) {
            profile.realName = request.realName();
            user.nickname = request.realName();
        }
        if (request.mobile() != null && !request.mobile().isBlank()) {
            user.mobile = request.mobile();
        }
        if (request.skillTags() != null) {
            profile.skillTags = assembler.joinList(request.skillTags());
        }
        if (request.serviceAreas() != null) {
            profile.serviceArea = assembler.joinList(request.serviceAreas());
        }
        if (request.deposit() != null) {
            profile.deposit = request.deposit();
        }
        if (request.online() != null) {
            profile.online = request.online();
        }
        if (request.listening() != null) {
            profile.listening = request.listening();
        }
        if (request.maxDistanceKm() != null) {
            profile.maxDistanceKm = request.maxDistanceKm();
        }
        if (request.privacyNumber() != null) {
            profile.privacyNumber = request.privacyNumber();
        }
        if (request.enabled() != null) {
            profile.enabled = request.enabled();
            if (!Boolean.TRUE.equals(request.enabled())) {
                profile.online = false;
            }
        }

        platformRepository.saveUser(user);
        platformRepository.saveMasterProfile(profile);
        return toMasterDetail(profile, user);
    }

    @Transactional
    public AdminBusinessDtos.MasterDetail enableMaster(Long userId) {
        MasterProfileEntity profile = platformRepository.findMasterProfileByUserId(userId).orElseThrow();
        UserEntity user = platformRepository.findUser(userId).orElseThrow();
        profile.enabled = true;
        platformRepository.saveMasterProfile(profile);
        return toMasterDetail(profile, user);
    }

    @Transactional
    public AdminBusinessDtos.MasterDetail disableMaster(Long userId) {
        MasterProfileEntity profile = platformRepository.findMasterProfileByUserId(userId).orElseThrow();
        UserEntity user = platformRepository.findUser(userId).orElseThrow();
        profile.enabled = false;
        profile.online = false;
        platformRepository.saveMasterProfile(profile);
        return toMasterDetail(profile, user);
    }

    @Transactional
    public AdminBusinessDtos.MasterDetail updateCreditScore(Long userId, AdminBusinessDtos.MasterCreditRequest request) {
        MasterProfileEntity profile = platformRepository.findMasterProfileByUserId(userId).orElseThrow();
        UserEntity user = platformRepository.findUser(userId).orElseThrow();
        profile.creditScore = request.creditScore();
        platformRepository.saveMasterProfile(profile);
        return toMasterDetail(profile, user);
    }

    public AdminBusinessDtos.FinanceDetail financeDetail(String billNo) {
        if (billNo.startsWith("SETTLE-")) {
            Long settlementId = Long.parseLong(billNo.substring("SETTLE-".length()));
            SettlementBillEntity bill = platformRepository.findSettlementBill(settlementId).orElse(null);
            if (bill != null) {
                return toSettlementFinanceDetail(bill);
            }
        } else if (billNo.startsWith("REFUND-")) {
            String orderId = billNo.substring("REFUND-".length());
            RefundRequestEntity refundRequest = platformRepository.findLatestRefundRequestByOrderId(orderId).orElse(null);
            if (refundRequest != null) {
                return toRefundFinanceDetail(refundRequest);
            }
        }

        if (billNo.startsWith("SETTLE-")) {
            Long transactionId = Long.parseLong(billNo.substring("SETTLE-".length()));
            WalletTransactionEntity transaction = platformRepository.findWalletTransaction(transactionId).orElseThrow();
            WalletAccountEntity account = platformRepository.findWalletAccount(transaction.walletAccountId).orElseThrow();
            UserEntity master = platformRepository.findUser(account.masterUserId).orElse(null);
            return new AdminBusinessDtos.FinanceDetail(
                    billNo,
                    "师傅结算",
                    transaction.statusText == null || transaction.statusText.isBlank() ? "待结算" : transaction.statusText,
                    transaction.amount,
                    extractOrderId(transaction.title),
                    transaction.title,
                    master == null ? "" : master.nickname,
                    transaction.transactionTime,
                    ""
            );
        }

        String orderId = billNo.substring("REFUND-".length());
        if (platformRepository.findServiceOrder(orderId).isPresent()) {
            ServiceOrderEntity order = platformRepository.findServiceOrder(orderId).orElseThrow();
            return new AdminBusinessDtos.FinanceDetail(
                    billNo,
                    "服务退款",
                    domainSupport.paymentStatusLabel(order.paymentStatus),
                    order.amount,
                    order.id,
                    order.title,
                    "",
                    order.updatedAt == null ? "" : order.updatedAt.toString(),
                    ""
            );
        }

        ProductOrderEntity order = platformRepository.findProductOrder(orderId).orElseThrow();
        return new AdminBusinessDtos.FinanceDetail(
                billNo,
                "商品退款",
                domainSupport.paymentStatusLabel(order.paymentStatus),
                order.amount,
                order.id,
                order.title,
                "",
                order.createdAt == null ? "" : order.createdAt.toString(),
                ""
        );
    }

    @Transactional
    public AdminBusinessDtos.FinanceDetail approveFinance(String billNo, AdminBusinessDtos.FinanceApproveRequest request) {
        if (billNo.startsWith("SETTLE-")) {
            Long settlementId = Long.parseLong(billNo.substring("SETTLE-".length()));
            SettlementBillEntity bill = platformRepository.findSettlementBill(settlementId).orElse(null);
            if (bill != null) {
                domainSupport.approveSettlement(bill, 90001L, request == null ? null : request.remark());
                return financeDetail(billNo);
            }
        } else if (billNo.startsWith("REFUND-")) {
            String orderId = billNo.substring("REFUND-".length());
            RefundRequestEntity refundRequest = platformRepository.findLatestRefundRequestByOrderId(orderId).orElse(null);
            if (refundRequest != null && refundRequest.status != RefundRequestStatus.COMPLETED) {
                wechatPaymentService.approveRefund(refundRequest, 90001L, request == null ? null : request.remark());
                return financeDetail(billNo);
            }
        }

        if (billNo.startsWith("SETTLE-")) {
            Long transactionId = Long.parseLong(billNo.substring("SETTLE-".length()));
            WalletTransactionEntity transaction = platformRepository.findWalletTransaction(transactionId).orElseThrow();
            transaction.statusText = "已结算";
            platformRepository.saveWalletTransaction(transaction);
            return financeDetail(billNo);
        }

        String orderId = billNo.substring("REFUND-".length());
        platformRepository.findServiceOrder(orderId).ifPresent(order -> {
            order.paymentStatus = PaymentStatus.REFUNDED;
            if (order.status == ServiceOrderStatus.REFUNDING) {
                order.status = ServiceOrderStatus.CANCELLED;
            }
            order.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(order);
        });

        platformRepository.findProductOrder(orderId).ifPresent(order -> {
            order.paymentStatus = PaymentStatus.REFUNDED;
            platformRepository.saveProductOrder(order);
        });

        return financeDetail(billNo);
    }

    public AdminBusinessDtos.ArbitrationDetail arbitrationDetail(String id) {
        ArbitrationCaseEntity arbitration = platformRepository.findArbitration(id).orElseThrow();
        String orderId = arbitration.orderId;
        String orderTitle = platformRepository.findServiceOrder(orderId).map(order -> order.title)
                .orElseGet(() -> platformRepository.findProductOrder(orderId).map(order -> order.title).orElse(""));

        int messageCount = platformRepository.listMessageSessions().stream()
                .filter(session -> orderId.equals(session.orderId))
                .map(session -> session.id)
                .mapToInt(sessionId -> platformRepository.listMessageItems(sessionId).size())
                .sum();

        List<AdminBusinessDtos.MediaItem> mediaItems = collectOrderMedia(orderId);
        return new AdminBusinessDtos.ArbitrationDetail(
                arbitration.id,
                orderId,
                orderTitle,
                arbitration.reasonText,
                arbitration.statusText == null || arbitration.statusText.isBlank() ? "待裁决" : arbitration.statusText,
                arbitration.resultText == null ? "" : arbitration.resultText,
                messageCount,
                buildTrackPoints(orderId),
                mediaItems
        );
    }

    @Transactional
    public AdminBusinessDtos.ArbitrationDetail resolveArbitration(String id, AdminBusinessDtos.ArbitrationResolveRequest request) {
        ArbitrationCaseEntity arbitration = platformRepository.findArbitration(id).orElseThrow();
        arbitration.statusText = request.statusText();
        arbitration.resultText = request.resultText();
        platformRepository.saveArbitration(arbitration);
        return arbitrationDetail(id);
    }

    private AdminBusinessDtos.OrderDetail buildServiceOrderDetail(ServiceOrderEntity order) {
        UserEntity user = platformRepository.findUser(order.userId).orElse(null);
        AddressEntity address = platformRepository.findAddress(order.addressId).orElse(null);
        QuotationEntity quotation = platformRepository.findQuotationByOrderId(order.id).orElse(null);
        boolean canCancel = order.status != ServiceOrderStatus.COMPLETED && order.status != ServiceOrderStatus.CANCELLED;
        boolean canRefund = order.paymentStatus == PaymentStatus.PARTIAL_PAID
                || order.paymentStatus == PaymentStatus.PAID
                || order.paymentStatus == PaymentStatus.REFUNDING;

        return new AdminBusinessDtos.OrderDetail(
                order.id,
                "SERVICE",
                order.title,
                domainSupport.statusLabel(order.status),
                domainSupport.paymentStatusLabel(order.paymentStatus),
                user == null ? "" : user.nickname,
                address == null ? "" : address.detailAddress,
                order.appointment,
                order.etaText,
                order.amount,
                "",
                canCancel,
                canRefund,
                true,
                true,
                buildQuotationView(quotation),
                buildMessageSummary(order.id),
                collectOrderMedia(order.id),
                buildTimeline(order.id),
                buildTrackPoints(order.id)
        );
    }

    private AdminBusinessDtos.OrderDetail buildProductOrderDetail(ProductOrderEntity order) {
        UserEntity user = platformRepository.findUser(order.userId).orElse(null);
        AddressEntity address = platformRepository.findAddress(order.addressId).orElse(null);
        boolean canRefund = order.paymentStatus == PaymentStatus.PAID
                || order.paymentStatus == PaymentStatus.REFUNDING
                || order.paymentStatus == PaymentStatus.REFUNDED;

        return new AdminBusinessDtos.OrderDetail(
                order.id,
                "PRODUCT",
                order.title,
                productStatusLabel(order.status),
                domainSupport.paymentStatusLabel(order.paymentStatus),
                user == null ? "" : user.nickname,
                address == null ? "" : address.detailAddress,
                "",
                "",
                order.amount,
                order.installServiceOrderId == null ? "" : order.installServiceOrderId,
                false,
                canRefund,
                true,
                false,
                null,
                buildMessageSummary(order.installServiceOrderId),
                order.installServiceOrderId == null ? List.of() : collectOrderMedia(order.installServiceOrderId),
                List.of(),
                List.of()
        );
    }

    private AdminBusinessDtos.MasterDetail toMasterDetail(MasterProfileEntity profile, UserEntity user) {
        return new AdminBusinessDtos.MasterDetail(
                profile.userId,
                profile.realName == null || profile.realName.isBlank() ? user.nickname : profile.realName,
                user.mobile,
                assembler.splitList(profile.skillTags),
                assembler.splitList(profile.serviceArea),
                profile.deposit == null ? BigDecimal.ZERO : profile.deposit,
                profile.creditScore == null ? 0 : profile.creditScore,
                Boolean.TRUE.equals(profile.online),
                Boolean.TRUE.equals(profile.listening),
                profile.maxDistanceKm == null ? 0 : profile.maxDistanceKm,
                Boolean.TRUE.equals(profile.privacyNumber),
                profile.enabled == null || Boolean.TRUE.equals(profile.enabled)
        );
    }

    private AdminBusinessDtos.QuotationView buildQuotationView(QuotationEntity quotation) {
        if (quotation == null) {
            return null;
        }
        return new AdminBusinessDtos.QuotationView(
                quotation.id,
                quotation.status == null ? "" : quotation.status.name(),
                quotation.remarkText,
                quotation.totalAmount,
                platformRepository.listQuotationItems(quotation.id).stream()
                        .map(item -> new AdminBusinessDtos.QuotationItemView(item.name, item.amount))
                        .toList()
        );
    }

    private AdminBusinessDtos.MessageSummary buildMessageSummary(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return null;
        }
        MessageSessionEntity session = findSessionByOrderId(orderId);
        if (session == null) {
            return null;
        }
        List<MessageItemEntity> items = platformRepository.listMessageItems(session.id);
        String latest = items.isEmpty() ? "" : items.get(items.size() - 1).contentText;
        String participant = platformRepository.findUser(session.participantUserId).map(user -> user.nickname).orElse("");
        return new AdminBusinessDtos.MessageSummary(session.id, session.title, participant, items.size(), latest);
    }

    private List<AdminBusinessDtos.TimelineItem> buildTimeline(String orderId) {
        return platformRepository.listWorkSteps(orderId).stream()
                .map(this::toTimelineItem)
                .toList();
    }

    private List<AdminBusinessDtos.TrackPoint> buildTrackPoints(String orderId) {
        return platformRepository.listTrackPoints(orderId).stream()
                .map(this::toTrackPoint)
                .toList();
    }

    private List<AdminBusinessDtos.MediaItem> collectOrderMedia(String orderId) {
        List<MediaFileEntity> entities = new ArrayList<>();
        entities.addAll(platformRepository.listMediaFilesByBiz("order_evidence", orderId));
        entities.addAll(platformRepository.listMediaFilesByBiz("before_work_media", orderId));
        entities.addAll(platformRepository.listMediaFilesByBiz("after_work_media", orderId));
        return toMediaItems(entities);
    }

    private List<AdminBusinessDtos.MediaItem> toMediaItems(List<MediaFileEntity> entities) {
        return entities.stream()
                .map(item -> new AdminBusinessDtos.MediaItem(
                        item.id,
                        item.bizType,
                        item.originalName,
                        item.accessUrl == null || item.accessUrl.isBlank() ? item.storagePath : item.accessUrl
                ))
                .toList();
    }

    private AdminBusinessDtos.TimelineItem toTimelineItem(WorkStepRecordEntity entity) {
        return new AdminBusinessDtos.TimelineItem(
                entity.stepKey,
                entity.labelText,
                entity.descriptionText,
                Boolean.TRUE.equals(entity.done),
                entity.stepTime
        );
    }

    private AdminBusinessDtos.TrackPoint toTrackPoint(OrderTrackPointEntity entity) {
        return new AdminBusinessDtos.TrackPoint(
                entity.id,
                entity.pointType,
                entity.labelText,
                entity.descriptionText,
                entity.latitude,
                entity.longitude,
                entity.createdAt
        );
    }

    private CouponEntity chooseCoupon(AdminBusinessDtos.GrantCouponRequest request) {
        return platformRepository.listCoupons().stream()
                .filter(item -> Boolean.TRUE.equals(item.enabled))
                .filter(item -> request.couponId() == null || item.id.equals(request.couponId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("没有可发放的优惠券。"));
    }

    private MessageSessionEntity findSessionByOrderId(String orderId) {
        return platformRepository.listMessageSessions().stream()
                .filter(item -> orderId.equals(item.orderId))
                .findFirst()
                .orElse(null);
    }

    private String resolveMasterName(Long masterUserId, String masterName) {
        if (masterName != null && !masterName.isBlank()) {
            return masterName;
        }
        if (masterUserId != null) {
            return platformRepository.findMasterProfileByUserId(masterUserId)
                    .map(profile -> profile.realName)
                    .orElseGet(() -> platformRepository.findUser(masterUserId).map(user -> user.nickname).orElse(""));
        }
        return platformRepository.findMasterProfileByUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID)
                .map(profile -> profile.realName)
                .orElse("");
    }

    private String extractOrderId(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }
        int blankIndex = title.indexOf(' ');
        return blankIndex > 0 ? title.substring(0, blankIndex) : "";
    }

    private AdminBusinessDtos.FinanceDetail toSettlementFinanceDetail(SettlementBillEntity bill) {
        UserEntity master = bill.masterId == null ? null : platformRepository.findUser(bill.masterId).orElse(null);
        String title = bill.remarkText == null || bill.remarkText.isBlank() ? bill.orderId : bill.remarkText;
        return new AdminBusinessDtos.FinanceDetail(
                "SETTLE-" + bill.id,
                "甯堝倕缁撶畻",
                settlementStatusLabel(bill.status),
                bill.amount,
                bill.orderId == null || bill.orderId.isBlank() ? extractOrderId(title) : bill.orderId,
                title,
                master == null ? "" : master.nickname,
                bill.updatedAt == null ? "" : bill.updatedAt.toString(),
                bill.bizNo == null ? "" : bill.bizNo
        );
    }

    private AdminBusinessDtos.FinanceDetail toRefundFinanceDetail(RefundRequestEntity refundRequest) {
        String title = platformRepository.findServiceOrder(refundRequest.orderId).map(order -> order.title)
                .orElseGet(() -> platformRepository.findProductOrder(refundRequest.orderId).map(order -> order.title).orElse(refundRequest.orderId));
        String type = "SERVICE".equalsIgnoreCase(refundRequest.orderType) ? "鏈嶅姟閫€娆?" : "鍟嗗搧閫€娆?";
        return new AdminBusinessDtos.FinanceDetail(
                "REFUND-" + refundRequest.orderId,
                type,
                refundStatusLabel(refundRequest.status),
                refundRequest.amount,
                refundRequest.orderId,
                title,
                "",
                refundRequest.updatedAt == null ? "" : refundRequest.updatedAt.toString(),
                refundRequest.bizNo == null ? "" : refundRequest.bizNo
        );
    }

    private String settlementStatusLabel(SettlementBillStatus status) {
        if (status == null) {
            return "待结算";
        }
        return switch (status) {
            case PENDING_REVIEW -> "待结算";
            case APPROVED -> "已结算";
            case REVERSED -> "已冲减";
        };
    }

    private String refundStatusLabel(RefundRequestStatus status) {
        if (status == null) {
            return "处理中";
        }
        return switch (status) {
            case PENDING_REVIEW -> "待审核";
            case APPROVED -> "待退款";
            case REJECTED -> "已拒绝";
            case COMPLETED -> "已退款";
        };
    }

    private String productStatusLabel(ProductOrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "待支付";
            case PAID -> "已支付";
            case PENDING_SHIPMENT -> "待发货";
            case SHIPPED -> "已发货";
            case COMPLETED -> "已完成";
            case REFUNDING -> "退款中";
        };
    }
}
