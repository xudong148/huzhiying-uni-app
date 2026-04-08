package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PlatformFacadeService platformFacadeService;

    public PaymentController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @PostMapping("/api/payments/wechat/prepay")
    public ApiResponse<?> prepay(@RequestBody PaymentRequest request) {
        return ApiResponse.success(platformFacadeService.createWechatPrepay(request.orderId()));
    }

    @PostMapping("/api/payments/wechat/refund")
    public ApiResponse<?> refund(@RequestBody PaymentRequest request) {
        return ApiResponse.success("退款已受理", platformFacadeService.refundOrder(request.orderId()));
    }

    @PostMapping("/api/payments/wechat/{orderId}/callback")
    public ApiResponse<?> callback(@PathVariable String orderId) {
        return ApiResponse.success(platformFacadeService.handleWechatCallback(orderId));
    }

    public record PaymentRequest(String orderId) {}
}
