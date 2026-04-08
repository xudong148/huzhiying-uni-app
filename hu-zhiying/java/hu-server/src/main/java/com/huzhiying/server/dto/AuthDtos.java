package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public final class AuthDtos {

    private AuthDtos() {}

    @Schema(description = "登录态中的用户资料")
    public record AuthProfilePayload(
            @Schema(description = "用户 ID", example = "90001")
            Long id,
            @Schema(description = "登录用户名", example = "admin")
            String username,
            @Schema(description = "显示名称", example = "运营后台")
            String name,
            @Schema(description = "手机号", example = "13900001234")
            String mobile,
            @Schema(description = "角色编码", example = "admin")
            String roleCode,
            @Schema(description = "角色名称", example = "超级管理员")
            String roleName
    ) {}

    @Schema(description = "后台菜单")
    public record AuthMenuPayload(
            @Schema(description = "菜单 ID", example = "1")
            Long id,
            @Schema(description = "菜单名称", example = "仪表盘")
            String name,
            @Schema(description = "菜单路径", example = "/dashboard")
            String path,
            @Schema(description = "图标", example = "mdi:view-dashboard")
            String icon
    ) {}

    @Schema(description = "统一登录态响应")
    public record AuthSessionPayload(
            @Schema(description = "访问令牌")
            String token,
            @Schema(description = "刷新令牌")
            String refreshToken,
            @Schema(description = "当前登录角色", example = "admin")
            String role,
            @Schema(description = "访问令牌过期时间")
            LocalDateTime accessExpiresAt,
            @Schema(description = "刷新令牌过期时间")
            LocalDateTime refreshExpiresAt,
            @Schema(implementation = AuthProfilePayload.class)
            AuthProfilePayload profile,
            @ArraySchema(schema = @Schema(implementation = AuthMenuPayload.class))
            List<AuthMenuPayload> menus,
            @ArraySchema(schema = @Schema(description = "权限编码"))
            List<String> permissions
    ) {}

    @Schema(description = "短信验证码发送结果")
    public record SmsCodePayload(
            @Schema(description = "手机号", example = "13900001234")
            String mobile,
            @Schema(description = "用途", example = "login")
            String purpose,
            @Schema(description = "过期秒数", example = "300")
            int expiresInSeconds
    ) {}
}
