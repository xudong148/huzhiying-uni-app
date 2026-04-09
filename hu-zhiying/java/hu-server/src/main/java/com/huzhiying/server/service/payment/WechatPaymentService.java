package com.huzhiying.server.service.payment;

import com.huzhiying.domain.enums.DomainEnums.PaymentRecordStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.RefundRequestStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.persistence.PersistenceEntities.PaymentRecordEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.RefundRequestEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.service.PlatformCommandService;
import com.huzhiying.server.service.PlatformDomainSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class WechatPaymentService {

    private static final String CHANNEL_WECHAT = "WECHAT";
    private static final String ORDER_TYPE_SERVICE = "SERVICE";
    private static final String ORDER_TYPE_PRODUCT = "PRODUCT";
    private static final String PAYMENT_STAGE_INITIAL = "INITIAL";
    private static final String PAYMENT_STAGE_SUPPLEMENT = "SUPPLEMENT";

    private final WechatPayProperties properties;
    private final WechatPaymentGateway gateway;
    private final PlatformRepository platformRepository;
    private final PlatformDomainSupport domainSupport;
    private final PlatformCommandService platformCommandService;

    public WechatPaymentService(WechatPayProperties properties,
                                WechatPaymentGateway gateway,
                                PlatformRepository platformRepository,
                                PlatformDomainSupport domainSupport,
                                PlatformCommandService platformCommandService) {
        this.properties = properties;
        this.gateway = gateway;
        this.platformRepository = platformRepository;
        this.domainSupport = domainSupport;
        this.platformCommandService = platformCommandService;
    }

    public SupportDtos.WechatPrepayPayload createPrepay(String orderId,
                                                        String channel,
                                                        String payerOpenId,
                                                        String authCode,
                                                        String clientIp) {
        assertPaymentConfigured();
        OrderPaymentContext context = resolveOrderPaymentContext(orderId);
        WechatPaymentGateway.TradeChannel tradeChannel = WechatPaymentGateway.TradeChannel.parse(channel);

        PaymentRecordEntity paymentRecord = domainSupport.preparePaymentRecord(
                context.orderId(),
                context.orderType(),
                context.amount(),
                context.userId(),
                context.masterId(),
                context.paymentStage(),
                CHANNEL_WECHAT,
                "wechat-prepay-" + tradeChannel.name().toLowerCase()
        );

        try {
            WechatPaymentGateway.PrepayResult result = gateway.createPrepay(new WechatPaymentGateway.PrepayCommand(
                    paymentRecord.bizNo,
                    tradeChannel,
                    context.description(),
                    context.amount(),
                    payerOpenId,
                    authCode,
                    clientIp,
                    context.orderId() + ":" + context.paymentStage(),
                    properties.transactionNotifyUrl()
            ));
            return new SupportDtos.WechatPrepayPayload(
                    context.orderId(),
                    result.channel().name(),
                    paymentRecord.bizNo,
                    result.appId(),
                    result.partnerId(),
                    result.prepayId(),
                    result.timeStamp(),
                    result.nonceStr(),
                    result.packageValue(),
                    result.signType(),
                    result.paySign(),
                    true,
                    "已创建微信预支付单，请在微信内完成支付"
            );
        } catch (RuntimeException exception) {
            domainSupport.markPaymentFailed(paymentRecord, exception.getMessage());
            throw exception;
        }
    }

    public void handleTransactionNotification(WechatPaymentGateway.NotificationRequest request) {
        WechatPaymentGateway.TransactionNotification notification = gateway.parseTransactionNotification(request);
        PaymentRecordEntity paymentRecord = platformRepository.findPaymentRecordByBizNo(notification.outTradeNo())
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的微信支付流水: " + notification.outTradeNo()));

        if (!"SUCCESS".equalsIgnoreCase(notification.tradeState())) {
            domainSupport.markPaymentFailed(paymentRecord, notification.tradeStateDesc());
            return;
        }
        platformCommandService.handleWechatPaymentSuccess(paymentRecord, notification.transactionId(), notification.tradeStateDesc());
    }

    public RefundRequestEntity approveRefund(RefundRequestEntity refundRequest, Long operatorId, String remark) {
        assertPaymentConfigured();
        PaymentRecordEntity paymentRecord = platformRepository.listPaymentRecordsByOrderId(refundRequest.orderId).stream()
                .filter(item -> item.status == PaymentRecordStatus.SUCCESS || item.status == PaymentRecordStatus.REFUNDING)
                .sorted((left, right) -> {
                    LocalDateTime leftTime = left.updatedAt == null ? left.createdAt : left.updatedAt;
                    LocalDateTime rightTime = right.updatedAt == null ? right.createdAt : right.updatedAt;
                    if (leftTime == null && rightTime == null) {
                        return 0;
                    }
                    if (leftTime == null) {
                        return 1;
                    }
                    if (rightTime == null) {
                        return -1;
                    }
                    return rightTime.compareTo(leftTime);
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("当前订单缺少可退款的支付流水"));

        if (refundRequest.status != RefundRequestStatus.APPROVED) {
            refundRequest.status = RefundRequestStatus.APPROVED;
            refundRequest.operatorId = operatorId;
            refundRequest.paymentRecordId = paymentRecord.id;
            refundRequest.approvedAt = refundRequest.approvedAt == null ? LocalDateTime.now() : refundRequest.approvedAt;
            refundRequest.updatedAt = LocalDateTime.now();
            platformRepository.saveRefundRequest(refundRequest);
            domainSupport.saveAuditLog(refundRequest.orderType, refundRequest.orderId, "REFUND_REQUEST_APPROVED",
                    refundRequest.status.name(), "ADMIN", operatorId, refundRequest.userId, refundRequest.masterId,
                    refundRequest.traceId, remark);
        }

        WechatPaymentGateway.RefundResult result = gateway.createRefund(new WechatPaymentGateway.RefundCommand(
                refundRequest.bizNo,
                paymentRecord.bizNo,
                paymentRecord.externalTransactionNo,
                refundRequest.amount,
                paymentRecord.amount == null ? refundRequest.amount : paymentRecord.amount,
                remark == null || remark.isBlank() ? refundRequest.reasonText : remark,
                properties.refundNotifyUrl()
        ));
        if ("SUCCESS".equalsIgnoreCase(result.status())) {
            platformCommandService.handleWechatRefundSuccess(refundRequest, result.refundId(), "wechat-refund-sync-success");
            return platformRepository.findRefundRequest(refundRequest.id).orElseThrow();
        }
        refundRequest.updatedAt = LocalDateTime.now();
        platformRepository.saveRefundRequest(refundRequest);
        return refundRequest;
    }

    public void handleRefundNotification(WechatPaymentGateway.NotificationRequest request) {
        WechatPaymentGateway.RefundNotification notification = gateway.parseRefundNotification(request);
        RefundRequestEntity refundRequest = platformRepository.findRefundRequestByBizNo(notification.outRefundNo())
                .orElseThrow(() -> new IllegalArgumentException("未找到对应的退款申请: " + notification.outRefundNo()));

        if ("SUCCESS".equalsIgnoreCase(notification.status())) {
            platformCommandService.handleWechatRefundSuccess(refundRequest, notification.refundId(), notification.status());
            return;
        }

        if ("PROCESSING".equalsIgnoreCase(notification.status())) {
            refundRequest.status = RefundRequestStatus.APPROVED;
            refundRequest.updatedAt = LocalDateTime.now();
            platformRepository.saveRefundRequest(refundRequest);
            return;
        }

        refundRequest.status = RefundRequestStatus.REJECTED;
        refundRequest.updatedAt = LocalDateTime.now();
        refundRequest.reasonText = notification.status()
                + (notification.userReceivedAccount() == null ? "" : " / " + notification.userReceivedAccount());
        platformRepository.saveRefundRequest(refundRequest);
        domainSupport.saveAuditLog(refundRequest.orderType, refundRequest.orderId, "REFUND_CALLBACK_FAILED",
                refundRequest.status.name(), "SYSTEM", null, refundRequest.userId, refundRequest.masterId,
                refundRequest.traceId, notification.rawBody());
    }

    private OrderPaymentContext resolveOrderPaymentContext(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("缺少订单号，无法创建预支付单。");
        }
        if (orderId.startsWith("SO")) {
            ServiceOrderEntity order = domainSupport.findServiceOrderEntity(orderId);
            if (order.status != ServiceOrderStatus.PENDING_PAYMENT
                    && order.status != ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT) {
                throw new IllegalStateException("当前服务订单不处于待支付状态，无需重复发起支付");
            }
            String paymentStage = order.status == ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT
                    ? PAYMENT_STAGE_SUPPLEMENT
                    : PAYMENT_STAGE_INITIAL;
            return new OrderPaymentContext(
                    order.id,
                    ORDER_TYPE_SERVICE,
                    paymentStage,
                    order.title == null || order.title.isBlank() ? order.id : order.title,
                    order.amount == null ? BigDecimal.ZERO : order.amount,
                    order.userId,
                    order.masterUserId
            );
        }
        if (orderId.startsWith("PO")) {
            ProductOrderEntity order = platformRepository.findProductOrder(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("商品订单不存在"));
            if (order.status != ProductOrderStatus.PENDING_PAYMENT) {
                throw new IllegalStateException("当前商品订单不处于待支付状态，无需重复发起支付");
            }
            return new OrderPaymentContext(
                    order.id,
                    ORDER_TYPE_PRODUCT,
                    PAYMENT_STAGE_INITIAL,
                    order.title == null || order.title.isBlank() ? order.id : order.title,
                    order.amount == null ? BigDecimal.ZERO : order.amount,
                    order.userId,
                    null
            );
        }
        throw new IllegalArgumentException("不支持的订单号格式");
    }

    private void assertPaymentConfigured() {
        if (!properties.isPaymentConfigured()) {
            throw new IllegalStateException("当前环境未配置微信支付商户参数，无法创建预支付单。");
        }
    }

    private record OrderPaymentContext(
            String orderId,
            String orderType,
            String paymentStage,
            String description,
            BigDecimal amount,
            Long userId,
            Long masterId
    ) {
    }
}
