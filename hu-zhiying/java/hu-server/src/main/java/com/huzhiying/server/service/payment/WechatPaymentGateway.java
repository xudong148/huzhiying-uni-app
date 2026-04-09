package com.huzhiying.server.service.payment;

import java.math.BigDecimal;

public interface WechatPaymentGateway {

    enum TradeChannel {
        JSAPI,
        APP;

        public static TradeChannel parse(String value) {
            if (value == null || value.isBlank()) {
                return JSAPI;
            }
            return switch (value.trim().toUpperCase()) {
                case "APP" -> APP;
                case "JSAPI" -> JSAPI;
                default -> throw new IllegalArgumentException("不支持的微信支付渠道: " + value);
            };
        }
    }

    record PrepayCommand(
            String outTradeNo,
            TradeChannel channel,
            String description,
            BigDecimal amount,
            String payerOpenId,
            String authCode,
            String clientIp,
            String attach,
            String notifyUrl
    ) {
    }

    record PrepayResult(
            TradeChannel channel,
            String appId,
            String timeStamp,
            String nonceStr,
            String packageValue,
            String signType,
            String paySign,
            String partnerId,
            String prepayId
    ) {
    }

    record NotificationRequest(
            String serialNumber,
            String timestamp,
            String nonce,
            String signature,
            String signType,
            String body
    ) {
    }

    record TransactionNotification(
            String outTradeNo,
            String transactionId,
            String tradeState,
            String tradeStateDesc,
            String rawBody
    ) {
    }

    record RefundCommand(
            String outRefundNo,
            String outTradeNo,
            String transactionId,
            BigDecimal refundAmount,
            BigDecimal totalAmount,
            String reason,
            String notifyUrl
    ) {
    }

    record RefundResult(
            String outRefundNo,
            String refundId,
            String status
    ) {
    }

    record RefundNotification(
            String outRefundNo,
            String outTradeNo,
            String refundId,
            String status,
            String userReceivedAccount,
            String rawBody
    ) {
    }

    PrepayResult createPrepay(PrepayCommand command);

    TransactionNotification parseTransactionNotification(NotificationRequest request);

    RefundResult createRefund(RefundCommand command);

    RefundNotification parseRefundNotification(NotificationRequest request);
}
