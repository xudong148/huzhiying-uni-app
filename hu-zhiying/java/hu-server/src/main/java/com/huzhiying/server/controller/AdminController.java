package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final PlatformFacadeService platformFacadeService;

    public AdminController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/admin/dashboard")
    public ApiResponse<?> dashboard() {
        return ApiResponse.success(platformFacadeService.adminDashboard());
    }

    @GetMapping("/api/admin/masters")
    public ApiResponse<?> masters() {
        return ApiResponse.success(platformFacadeService.masters());
    }

    @GetMapping("/api/admin/arbitrations")
    public ApiResponse<?> arbitrations() {
        return ApiResponse.success(platformFacadeService.arbitrations());
    }

    @GetMapping("/api/admin/pricing")
    public ApiResponse<?> pricing() {
        return ApiResponse.success(platformFacadeService.pricingRules());
    }
}
