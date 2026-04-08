package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "payment", description = "微信支付接口")
public class PaymentController {

    private final PlatformFacadeService platformFacadeService;

    public PaymentController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @PostMapping("/api/payments/wechat/prepay")
    @Operation(summary = "创建微信预支付单")
    public ResponseEntity<ApiResponse<SupportDtos.WechatPrepayPayload>> prepay(@RequestBody PaymentRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.success((SupportDtos.WechatPrepayPayload) platformFacadeService.createWechatPrepay(request.orderId())));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(exception.getMessage()));
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ApiResponse.fail(exception.getMessage()));
        }
    }

    @PostMapping("/api/payments/wechat/refund")
    @Operation(summary = "发起微信退款")
    public ApiResponse<?> refund(@RequestBody PaymentRequest request) {
        return ApiResponse.success("退款申请已受理", platformFacadeService.refundOrder(request.orderId()));
    }

    @PostMapping("/api/payments/wechat/{orderId}/callback")
    @Operation(summary = "接收微信支付回调")
    public ApiResponse<?> callback(@PathVariable("orderId") String orderId) {
        return ApiResponse.success(platformFacadeService.handleWechatCallback(orderId));
    }

    public record PaymentRequest(String orderId) {}
}
