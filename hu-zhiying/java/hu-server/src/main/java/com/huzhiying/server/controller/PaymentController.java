package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.service.PlatformFacadeService;
import com.huzhiying.server.service.payment.WechatPaymentGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "payment", description = "微信支付接口")
public class PaymentController extends ApiControllerSupport {

    private final PlatformFacadeService platformFacadeService;

    public PaymentController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @PostMapping("/api/payments/wechat/prepay")
    @Operation(summary = "创建微信预支付单")
    public ResponseEntity<ApiResponse<SupportDtos.WechatPrepayPayload>> prepay(@RequestBody PaymentRequest request,
                                                                               HttpServletRequest httpServletRequest) {
        try {
            return ResponseEntity.ok(ApiResponse.success((SupportDtos.WechatPrepayPayload) platformFacadeService.createWechatPrepay(
                    request.orderId(),
                    request.channel(),
                    request.payerOpenId(),
                    request.authCode(),
                    resolveClientIp(httpServletRequest, request.clientIp())
            )));
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ApiResponse.fail(exception.getMessage()));
        }
    }

    @PostMapping("/api/payments/wechat/refund")
    @Operation(summary = "发起微信退款申请")
    public ApiResponse<?> refund(@RequestBody PaymentRequest request) {
        return ApiResponse.success("退款申请已受理", platformFacadeService.refundOrder(
                request.orderId(),
                request.reason(),
                request.remark(),
                request.source(),
                request.evidenceFileIds()
        ));
    }

    @PostMapping("/api/payments/wechat/notify/transactions")
    @Operation(summary = "接收微信支付结果通知")
    public ResponseEntity<Void> transactionNotify(
            @RequestHeader("Wechatpay-Serial") String serialNumber,
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestHeader(value = "Wechatpay-Signature-Type", required = false) String signType,
            @RequestBody String body) {
        platformFacadeService.handleWechatTransactionNotification(new WechatPaymentGateway.NotificationRequest(
                serialNumber,
                timestamp,
                nonce,
                signature,
                signType,
                body
        ));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/payments/wechat/notify/refunds")
    @Operation(summary = "接收微信退款结果通知")
    public ResponseEntity<Void> refundNotify(
            @RequestHeader("Wechatpay-Serial") String serialNumber,
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestHeader(value = "Wechatpay-Signature-Type", required = false) String signType,
            @RequestBody String body) {
        platformFacadeService.handleWechatRefundNotification(new WechatPaymentGateway.NotificationRequest(
                serialNumber,
                timestamp,
                nonce,
                signature,
                signType,
                body
        ));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/payments/wechat/{orderId}/callback")
    @Operation(summary = "兼容保留：本地触发支付回调")
    public ApiResponse<?> callback(@PathVariable("orderId") String orderId) {
        return ApiResponse.success(platformFacadeService.handleWechatCallback(orderId));
    }

    private String resolveClientIp(HttpServletRequest request, String requestClientIp) {
        if (requestClientIp != null && !requestClientIp.isBlank()) {
            return requestClientIp.trim();
        }
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            int separatorIndex = forwardedFor.indexOf(',');
            return separatorIndex > 0 ? forwardedFor.substring(0, separatorIndex).trim() : forwardedFor.trim();
        }
        return request.getRemoteAddr();
    }

    public record PaymentRequest(
            String orderId,
            String channel,
            String payerOpenId,
            String authCode,
            String clientIp,
            String reason,
            String remark,
            String source,
            List<Long> evidenceFileIds
    ) {
    }
}
