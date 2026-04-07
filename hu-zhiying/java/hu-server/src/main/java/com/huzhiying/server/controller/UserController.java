package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final PlatformFacadeService platformFacadeService;

    public UserController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/users/me")
    public ApiResponse<?> me() {
        return ApiResponse.success(platformFacadeService.currentUser());
    }

    @GetMapping("/api/addresses")
    public ApiResponse<?> addresses() {
        return ApiResponse.success(platformFacadeService.addresses());
    }

    @GetMapping("/api/coupons")
    public ApiResponse<?> coupons() {
        return ApiResponse.success(platformFacadeService.coupons());
    }

    @GetMapping("/api/members/current")
    public ApiResponse<?> member() {
        return ApiResponse.success(platformFacadeService.currentMember());
    }
}
