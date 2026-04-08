package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "认证与登录接口")
public class AuthController {

    private final PlatformFacadeService platformFacadeService;

    public AuthController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @PostMapping("/wechat-login")
    @Operation(summary = "微信登录")
    public ApiResponse<?> wechatLogin(@RequestBody LoginRequest request) {
        return ApiResponse.success(platformFacadeService.login(request.role()));
    }

    @PostMapping("/sms-login")
    @Operation(summary = "短信登录")
    public ApiResponse<?> smsLogin(@RequestBody LoginRequest request) {
        return ApiResponse.success(platformFacadeService.login(request.role()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新登录态")
    public ApiResponse<?> refresh(@RequestBody RefreshRequest request) {
        return ApiResponse.success(platformFacadeService.login("user"));
    }

    public record LoginRequest(@NotBlank String role) {}

    public record RefreshRequest(@NotBlank String refreshToken) {}
}
