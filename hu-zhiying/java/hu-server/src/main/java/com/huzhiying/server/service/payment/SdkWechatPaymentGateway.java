package com.huzhiying.server.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.Refund;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class SdkWechatPaymentGateway implements WechatPaymentGateway {

    private final WechatPayProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    private volatile RSAAutoCertificateConfig config;
    private volatile NotificationParser notificationParser;
    private volatile com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension jsapiServiceExtension;
    private volatile AppServiceExtension appServiceExtension;
    private volatile RefundService refundService;

    public SdkWechatPaymentGateway(WechatPayProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Override
    public PrepayResult createPrepay(PrepayCommand command) {
        return switch (command.channel()) {
            case JSAPI -> createJsapiPrepay(command);
            case APP -> createAppPrepay(command);
        };
    }

    @Override
    public TransactionNotification parseTransactionNotification(NotificationRequest request) {
        Transaction transaction = parser().parse(toRequestParam(request), Transaction.class);
        return new TransactionNotification(
                transaction.getOutTradeNo(),
                transaction.getTransactionId(),
                transaction.getTradeState() == null ? "" : transaction.getTradeState().name(),
                transaction.getTradeStateDesc(),
                request.body()
        );
    }

    @Override
    public RefundResult createRefund(RefundCommand command) {
        com.wechat.pay.java.service.refund.model.CreateRequest request =
                new com.wechat.pay.java.service.refund.model.CreateRequest();
        request.setOutRefundNo(command.outRefundNo());
        if (hasText(command.transactionId())) {
            request.setTransactionId(command.transactionId());
        } else {
            request.setOutTradeNo(command.outTradeNo());
        }
        request.setReason(command.reason());
        request.setNotifyUrl(command.notifyUrl());

        com.wechat.pay.java.service.refund.model.AmountReq amount =
                new com.wechat.pay.java.service.refund.model.AmountReq();
        amount.setRefund(toFen(command.refundAmount()).longValue());
        amount.setTotal(toFen(command.totalAmount()).longValue());
        amount.setCurrency("CNY");
        request.setAmount(amount);

        Refund refund = refundService().create(request);
        return new RefundResult(
                refund.getOutRefundNo(),
                refund.getRefundId(),
                refund.getStatus() == null ? "" : refund.getStatus().name()
        );
    }

    @Override
    public RefundNotification parseRefundNotification(NotificationRequest request) {
        Refund refund = parser().parse(toRequestParam(request), Refund.class);
        return new RefundNotification(
                refund.getOutRefundNo(),
                refund.getOutTradeNo(),
                refund.getRefundId(),
                refund.getStatus() == null ? "" : refund.getStatus().name(),
                refund.getUserReceivedAccount(),
                request.body()
        );
    }

    private PrepayResult createJsapiPrepay(PrepayCommand command) {
        String payerOpenId = resolvePayerOpenId(command);
        com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest request =
                new com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest();
        request.setAppid(properties.getAppId());
        request.setMchid(properties.getMchId());
        request.setDescription(command.description());
        request.setOutTradeNo(command.outTradeNo());
        request.setTimeExpire(java.time.OffsetDateTime.now().plusMinutes(Math.max(properties.getExpireMinutes(), 5)).toString());
        request.setAttach(command.attach());
        request.setNotifyUrl(command.notifyUrl());

        com.wechat.pay.java.service.payments.jsapi.model.Amount amount =
                new com.wechat.pay.java.service.payments.jsapi.model.Amount();
        amount.setTotal(toFen(command.amount()));
        amount.setCurrency("CNY");
        request.setAmount(amount);

        com.wechat.pay.java.service.payments.jsapi.model.Payer payer =
                new com.wechat.pay.java.service.payments.jsapi.model.Payer();
        payer.setOpenid(payerOpenId);
        request.setPayer(payer);

        if (hasText(command.clientIp())) {
            com.wechat.pay.java.service.payments.jsapi.model.SceneInfo sceneInfo =
                    new com.wechat.pay.java.service.payments.jsapi.model.SceneInfo();
            sceneInfo.setPayerClientIp(command.clientIp());
            request.setSceneInfo(sceneInfo);
        }

        com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse response =
                jsapiService().prepayWithRequestPayment(request);
        return new PrepayResult(
                command.channel(),
                response.getAppId(),
                response.getTimeStamp(),
                response.getNonceStr(),
                response.getPackageVal(),
                response.getSignType(),
                response.getPaySign(),
                properties.getMchId(),
                extractPrepayId(response.getPackageVal())
        );
    }

    private PrepayResult createAppPrepay(PrepayCommand command) {
        com.wechat.pay.java.service.payments.app.model.PrepayRequest request =
                new com.wechat.pay.java.service.payments.app.model.PrepayRequest();
        request.setAppid(properties.getAppId());
        request.setMchid(properties.getMchId());
        request.setDescription(command.description());
        request.setOutTradeNo(command.outTradeNo());
        request.setTimeExpire(java.time.OffsetDateTime.now().plusMinutes(Math.max(properties.getExpireMinutes(), 5)).toString());
        request.setAttach(command.attach());
        request.setNotifyUrl(command.notifyUrl());

        com.wechat.pay.java.service.payments.app.model.Amount amount =
                new com.wechat.pay.java.service.payments.app.model.Amount();
        amount.setTotal(toFen(command.amount()));
        amount.setCurrency("CNY");
        request.setAmount(amount);

        com.wechat.pay.java.service.payments.app.model.PrepayWithRequestPaymentResponse response =
                appService().prepayWithRequestPayment(request);
        return new PrepayResult(
                command.channel(),
                response.getAppid(),
                response.getTimestamp(),
                response.getNonceStr(),
                response.getPackageVal(),
                "RSA",
                response.getSign(),
                response.getPartnerId(),
                response.getPrepayId()
        );
    }

    private String resolvePayerOpenId(PrepayCommand command) {
        if (hasText(command.payerOpenId())) {
            return command.payerOpenId().trim();
        }
        if (!hasText(command.authCode())) {
            throw new IllegalArgumentException("JSAPI 支付缺少 payerOpenId 或 authCode");
        }
        if (!properties.isMiniProgramSessionConfigured()) {
            throw new IllegalStateException("当前环境未配置微信小程序 appSecret，无法通过 authCode 换取 openId");
        }

        String url = "https://api.weixin.qq.com/sns/jscode2session"
                + "?appid=" + encode(properties.getAppId())
                + "&secret=" + encode(properties.getAppSecret())
                + "&js_code=" + encode(command.authCode())
                + "&grant_type=authorization_code";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonNode body = objectMapper.readTree(response.body());
            if (body.hasNonNull("errcode") && body.get("errcode").asInt() != 0) {
                throw new IllegalStateException("微信 authCode 换取 openId 失败: " + body.path("errmsg").asText("未知错误"));
            }
            String openId = body.path("openid").asText("");
            if (!hasText(openId)) {
                throw new IllegalStateException("微信 authCode 换取 openId 失败: 响应缺少 openid");
            }
            return openId;
        } catch (IOException exception) {
            throw new IllegalStateException("微信 authCode 换取 openId 失败: 解析响应异常", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("微信 authCode 换取 openId 失败: 请求被中断", exception);
        }
    }

    private RSAAutoCertificateConfig config() {
        RSAAutoCertificateConfig current = config;
        if (current != null) {
            return current;
        }
        synchronized (this) {
            if (config == null) {
                RSAAutoCertificateConfig.Builder builder = new RSAAutoCertificateConfig.Builder()
                        .merchantId(properties.getMchId())
                        .merchantSerialNumber(properties.getMerchantSerialNumber())
                        .apiV3Key(properties.getApiV3Key());
                if (hasText(properties.getPrivateKeyPath())) {
                    builder.privateKeyFromPath(properties.getPrivateKeyPath());
                } else {
                    builder.privateKey(properties.normalizedPrivateKey());
                }
                config = builder.build();
            }
            return config;
        }
    }

    private NotificationParser parser() {
        NotificationParser current = notificationParser;
        if (current != null) {
            return current;
        }
        synchronized (this) {
            if (notificationParser == null) {
                notificationParser = new NotificationParser(config());
            }
            return notificationParser;
        }
    }

    private com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension jsapiService() {
        var current = jsapiServiceExtension;
        if (current != null) {
            return current;
        }
        synchronized (this) {
            if (jsapiServiceExtension == null) {
                jsapiServiceExtension = new com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension.Builder()
                        .config(config())
                        .build();
            }
            return jsapiServiceExtension;
        }
    }

    private AppServiceExtension appService() {
        AppServiceExtension current = appServiceExtension;
        if (current != null) {
            return current;
        }
        synchronized (this) {
            if (appServiceExtension == null) {
                appServiceExtension = new AppServiceExtension.Builder()
                        .config(config())
                        .build();
            }
            return appServiceExtension;
        }
    }

    private RefundService refundService() {
        RefundService current = refundService;
        if (current != null) {
            return current;
        }
        synchronized (this) {
            if (refundService == null) {
                refundService = new RefundService.Builder()
                        .config(config())
                        .build();
            }
            return refundService;
        }
    }

    private RequestParam toRequestParam(NotificationRequest request) {
        return new RequestParam.Builder()
                .serialNumber(request.serialNumber())
                .timestamp(request.timestamp())
                .nonce(request.nonce())
                .signature(request.signature())
                .signType(request.signType())
                .body(request.body())
                .build();
    }

    private Integer toFen(BigDecimal amount) {
        return amount == null
                ? 0
                : amount.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValueExact();
    }

    private String extractPrepayId(String packageValue) {
        if (!hasText(packageValue)) {
            return "";
        }
        int separatorIndex = packageValue.indexOf('=');
        return separatorIndex >= 0 ? packageValue.substring(separatorIndex + 1) : packageValue;
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
