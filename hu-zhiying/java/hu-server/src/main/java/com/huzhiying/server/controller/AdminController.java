package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "admin-dashboard", description = "后台仪表盘与业务列表接口")
public class AdminController {

    private final PlatformFacadeService platformFacadeService;

    public AdminController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/admin/dashboard")
    @Operation(summary = "查询后台仪表盘")
    public ApiResponse<?> dashboard() {
        return ApiResponse.success(platformFacadeService.adminDashboard());
    }

    @GetMapping("/api/admin/dispatch")
    @Operation(summary = "查询后台调度列表")
    public ApiResponse<?> dispatch() {
        return ApiResponse.success(platformFacadeService.adminDispatchRows());
    }

    @GetMapping("/api/admin/orders")
    @Operation(summary = "查询后台订单列表")
    public ApiResponse<?> orders() {
        return ApiResponse.success(platformFacadeService.adminOrders());
    }

    @GetMapping("/api/admin/masters")
    @Operation(summary = "查询师傅管理列表")
    public ApiResponse<?> masters() {
        return ApiResponse.success(platformFacadeService.masters());
    }

    @GetMapping("/api/admin/arbitrations")
    @Operation(summary = "查询仲裁工单列表")
    public ApiResponse<?> arbitrations() {
        return ApiResponse.success(platformFacadeService.arbitrations());
    }

    @GetMapping("/api/admin/pricing")
    @Operation(summary = "查询定价概览")
    public ApiResponse<?> pricing() {
        return ApiResponse.success(platformFacadeService.pricingRules());
    }

    @GetMapping("/api/admin/finance")
    @Operation(summary = "查询财务列表")
    public ApiResponse<?> finance() {
        return ApiResponse.success(platformFacadeService.financeRows());
    }
}
