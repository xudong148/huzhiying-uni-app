package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OrderController {

    private final PlatformFacadeService platformFacadeService;

    public OrderController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/service-orders")
    public ApiResponse<?> serviceOrders() {
        return ApiResponse.success(platformFacadeService.serviceOrders());
    }

    @GetMapping("/api/service-orders/{id}")
    public ApiResponse<?> serviceOrder(@PathVariable String id) {
        return ApiResponse.success(platformFacadeService.serviceOrder(id));
    }

    @PostMapping("/api/service-orders")
    public ApiResponse<?> createServiceOrder(@RequestBody CreateServiceOrderRequest request) {
        return ApiResponse.success(platformFacadeService.createServiceOrder(
                request.title(), request.appointment(), request.emergency(), request.nightService()));
    }

    @PostMapping("/api/service-orders/{id}/status")
    public ApiResponse<?> updateServiceOrderStatus(@PathVariable String id, @RequestBody StatusRequest request) {
        return ApiResponse.success(platformFacadeService.updateServiceOrderStatus(id, ServiceOrderStatus.valueOf(request.status())));
    }

    @GetMapping("/api/product-orders")
    public ApiResponse<?> productOrders() {
        return ApiResponse.success(platformFacadeService.productOrders());
    }

    @GetMapping("/api/product-orders/{id}")
    public ApiResponse<?> productOrder(@PathVariable String id) {
        return ApiResponse.success(platformFacadeService.productOrder(id));
    }

    @PostMapping("/api/quotations")
    public ApiResponse<?> createQuotation(@RequestBody CreateQuotationRequest request) {
        return ApiResponse.success(platformFacadeService.createQuotation(request.orderId(), request.remark()));
    }

    @PostMapping("/api/quotations/{quotationId}/confirm")
    public ApiResponse<?> confirmQuotation(@PathVariable String quotationId) {
        return ApiResponse.success(platformFacadeService.confirmQuotation(quotationId));
    }

    public record CreateServiceOrderRequest(String title, String appointment, boolean emergency, boolean nightService) {}

    public record StatusRequest(String status) {}

    public record CreateQuotationRequest(String orderId, String remark) {}
}
