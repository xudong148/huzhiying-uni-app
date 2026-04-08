package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.AuthDtos;
import com.huzhiying.server.service.AuthenticationService;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "认证与登录接口")
public class AuthController extends AuthControllerSupport {

    private final PlatformFacadeService platformFacadeService;
    private final AuthenticationService authenticationService;

    public AuthController(PlatformFacadeService platformFacadeService, AuthenticationService authenticationService) {
        this.platformFacadeService = platformFacadeService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/admin-login")
    @Operation(summary = "后台账号密码登录")
    public ApiResponse<AuthDtos.AuthSessionPayload> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(authenticationService.adminLogin(request.username(), request.password()));
    }

    @PostMapping("/send-code")
    @Operation(summary = "发送手机验证码")
    public ApiResponse<AuthDtos.SmsCodePayload> sendCode(@Valid @RequestBody SmsCodeRequest request) {
        return ApiResponse.success(authenticationService.sendSmsCode(request.mobile(), request.purpose()));
    }

    @PostMapping("/mobile-login")
    @Operation(summary = "手机号验证码登录")
    public ApiResponse<AuthDtos.AuthSessionPayload> mobileLogin(@Valid @RequestBody MobileLoginRequest request) {
        return ApiResponse.success(authenticationService.mobileLogin(request.mobile(), request.code(), request.role()));
    }

    @PostMapping("/register")
    @Operation(summary = "手机号注册")
    public ApiResponse<AuthDtos.AuthSessionPayload> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(authenticationService.register(request.mobile(), request.code(), request.nickname()));
    }

    @GetMapping("/session")
    @Operation(summary = "读取当前登录态")
    public ApiResponse<AuthDtos.AuthSessionPayload> session() {
        return ApiResponse.success(authenticationService.currentAccessSession());
    }

    @PostMapping("/wechat-login")
    @Operation(summary = "兼容保留：微信登录")
    public ApiResponse<?> wechatLogin(@RequestBody LoginRequest request) {
        return ApiResponse.success(platformFacadeService.login(request.role()));
    }

    @PostMapping("/sms-login")
    @Operation(summary = "兼容保留：短信登录")
    public ApiResponse<?> smsLogin(@RequestBody LoginRequest request) {
        return ApiResponse.success(platformFacadeService.login(request.role()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新登录态")
    public ApiResponse<AuthDtos.AuthSessionPayload> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.success(authenticationService.refresh(request.refreshToken(), request.clientType()));
    }

    public record AdminLoginRequest(
            @NotBlank(message = "用户名不能为空")
            String username,
            @NotBlank(message = "密码不能为空")
            String password
    ) {}

    public record SmsCodeRequest(
            @NotBlank(message = "手机号不能为空")
            String mobile,
            @NotBlank(message = "验证码用途不能为空")
            String purpose
    ) {}

    public record MobileLoginRequest(
            @NotBlank(message = "手机号不能为空")
            String mobile,
            @NotBlank(message = "短信验证码不能为空")
            String code,
            String role
    ) {}

    public record RegisterRequest(
            @NotBlank(message = "手机号不能为空")
            String mobile,
            @NotBlank(message = "短信验证码不能为空")
            String code,
            @NotBlank(message = "昵称不能为空")
            String nickname
    ) {}

    public record LoginRequest(String role) {}

    public record RefreshRequest(
            @NotBlank(message = "刷新令牌不能为空")
            String refreshToken,
            String clientType
    ) {}
}
