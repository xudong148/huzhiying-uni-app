package com.huzhiying.server.service.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hzy.wechat")
public class WechatPayProperties {

    private boolean payEnabled;
    private String appId = "";
    private String mchId = "";
    private String merchantSerialNumber = "";
    private String privateKey = "";
    private String privateKeyPath = "";
    private String apiV3Key = "";
    private String appSecret = "";
    private String notifyBaseUrl = "";
    private String transactionNotifyPath = "/api/payments/wechat/notify/transactions";
    private String refundNotifyPath = "/api/payments/wechat/notify/refunds";
    private int expireMinutes = 30;

    public boolean isPayEnabled() {
        return payEnabled;
    }

    public void setPayEnabled(boolean payEnabled) {
        this.payEnabled = payEnabled;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMerchantSerialNumber() {
        return merchantSerialNumber;
    }

    public void setMerchantSerialNumber(String merchantSerialNumber) {
        this.merchantSerialNumber = merchantSerialNumber;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public void setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getNotifyBaseUrl() {
        return notifyBaseUrl;
    }

    public void setNotifyBaseUrl(String notifyBaseUrl) {
        this.notifyBaseUrl = notifyBaseUrl;
    }

    public String getTransactionNotifyPath() {
        return transactionNotifyPath;
    }

    public void setTransactionNotifyPath(String transactionNotifyPath) {
        this.transactionNotifyPath = transactionNotifyPath;
    }

    public String getRefundNotifyPath() {
        return refundNotifyPath;
    }

    public void setRefundNotifyPath(String refundNotifyPath) {
        this.refundNotifyPath = refundNotifyPath;
    }

    public int getExpireMinutes() {
        return expireMinutes;
    }

    public void setExpireMinutes(int expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public boolean isPaymentConfigured() {
        return payEnabled
                && hasText(appId)
                && hasText(mchId)
                && hasText(merchantSerialNumber)
                && (hasText(privateKeyPath) || hasText(privateKey))
                && hasText(apiV3Key)
                && hasText(notifyBaseUrl);
    }

    public boolean isMiniProgramSessionConfigured() {
        return hasText(appSecret);
    }

    public String normalizedPrivateKey() {
        return privateKey == null ? "" : privateKey.replace("\\n", "\n").trim();
    }

    public String transactionNotifyUrl() {
        return buildNotifyUrl(transactionNotifyPath);
    }

    public String refundNotifyUrl() {
        return buildNotifyUrl(refundNotifyPath);
    }

    private String buildNotifyUrl(String path) {
        if (!hasText(notifyBaseUrl)) {
            return "";
        }
        String base = notifyBaseUrl.endsWith("/") ? notifyBaseUrl.substring(0, notifyBaseUrl.length() - 1) : notifyBaseUrl;
        String suffix = path == null || path.isBlank() ? "" : (path.startsWith("/") ? path : "/" + path);
        return base + suffix;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
