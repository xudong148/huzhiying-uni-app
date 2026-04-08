package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "mobile-order", description = "移动端订单、报价与轨迹接口")
public class OrderController {

    private final PlatformFacadeService platformFacadeService;

    public OrderController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/service-orders")
    @Operation(summary = "查询服务订单列表")
    public ApiResponse<?> serviceOrders() {
        return ApiResponse.success(platformFacadeService.serviceOrders());
    }

    @GetMapping("/api/service-orders/{id}")
    @Operation(summary = "查询服务订单详情")
    public ApiResponse<?> serviceOrder(@PathVariable("id") String id) {
        return ApiResponse.success(platformFacadeService.serviceOrder(id));
    }

    @PostMapping("/api/service-orders")
    @Operation(summary = "创建服务订单")
    public ApiResponse<?> createServiceOrder(@RequestBody CreateServiceOrderRequest request) {
        return ApiResponse.success(platformFacadeService.createServiceOrder(
                request.serviceItemId(),
                request.title(),
                request.appointment(),
                request.addressId(),
                request.description(),
                request.emergency(),
                request.nightService(),
                request.evidenceFileIds()
        ));
    }

    @PostMapping("/api/service-orders/{id}/status")
    @Operation(summary = "更新服务订单状态")
    public ApiResponse<?> updateServiceOrderStatus(@PathVariable("id") String id, @RequestBody StatusRequest request) {
        return ApiResponse.success(platformFacadeService.updateServiceOrderStatus(id, ServiceOrderStatus.valueOf(request.status())));
    }

    @GetMapping("/api/orders/{id}/tracking")
    @Operation(summary = "查询订单轨迹")
    public ApiResponse<?> tracking(@PathVariable("id") String id) {
        return ApiResponse.success(platformFacadeService.orderTracking(id));
    }

    @PostMapping("/api/orders/{id}/cancel")
    @Operation(summary = "取消订单")
    public ApiResponse<?> cancel(@PathVariable("id") String id, @RequestBody SupportDtos.CancelOrderRequest request) {
        return ApiResponse.success(platformFacadeService.cancelOrder(id, request.reason()));
    }

    @PostMapping("/api/orders/{id}/urge")
    @Operation(summary = "催单")
    public ApiResponse<?> urge(@PathVariable("id") String id, @RequestBody SupportDtos.UrgeOrderRequest request) {
        return ApiResponse.success(platformFacadeService.urgeOrder(id, request.remark()));
    }

    @GetMapping("/api/product-orders")
    @Operation(summary = "查询商品订单列表")
    public ApiResponse<?> productOrders() {
        return ApiResponse.success(platformFacadeService.productOrders());
    }

    @GetMapping("/api/product-orders/{id}")
    @Operation(summary = "查询商品订单详情")
    public ApiResponse<?> productOrder(@PathVariable("id") String id) {
        return ApiResponse.success(platformFacadeService.productOrder(id));
    }

    @PostMapping("/api/product-orders")
    @Operation(summary = "创建商品订单")
    public ApiResponse<?> createProductOrder(@RequestBody CreateProductOrderRequest request) {
        return ApiResponse.success(platformFacadeService.createProductOrder(
                request.productId(),
                request.skuId(),
                request.addressId()
        ));
    }

    @PostMapping("/api/quotations")
    @Operation(summary = "创建增项报价")
    public ApiResponse<?> createQuotation(@RequestBody CreateQuotationRequest request) {
        return ApiResponse.success(platformFacadeService.createQuotation(request.orderId(), request.remark()));
    }

    @PostMapping("/api/quotations/{quotationId}/confirm")
    @Operation(summary = "确认增项报价")
    public ApiResponse<?> confirmQuotation(@PathVariable("quotationId") String quotationId) {
        return ApiResponse.success(platformFacadeService.confirmQuotation(quotationId));
    }

    public record CreateServiceOrderRequest(Long serviceItemId, String title, String appointment, Long addressId,
                                            String description, boolean emergency, boolean nightService,
                                            List<Long> evidenceFileIds) {}

    public record CreateProductOrderRequest(Long productId, Long skuId, Long addressId) {}

    public record StatusRequest(String status) {}

    public record CreateQuotationRequest(String orderId, String remark) {}
}
