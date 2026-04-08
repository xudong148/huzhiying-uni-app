package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.AdminConfigDtos;
import com.huzhiying.server.service.AdminConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台权限与菜单配置接口。
 */
@RestController
@Tag(name = "admin-system", description = "后台系统权限配置")
public class AdminSystemController {

    private final AdminConfigService adminConfigService;

    public AdminSystemController(AdminConfigService adminConfigService) {
        this.adminConfigService = adminConfigService;
    }

    @GetMapping("/api/admin/system/roles")
    @Operation(summary = "查询角色列表")
    public ApiResponse<List<AdminConfigDtos.RolePayload>> roles() {
        return ApiResponse.success(adminConfigService.listRoles());
    }

    @GetMapping("/api/admin/system/roles/{id}")
    @Operation(summary = "查询角色详情")
    public ApiResponse<AdminConfigDtos.RolePayload> role(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getRole(id));
    }

    @PostMapping("/api/admin/system/roles")
    @Operation(summary = "新增角色")
    public ApiResponse<AdminConfigDtos.RolePayload> createRole(@RequestBody AdminConfigDtos.RolePayload payload) {
        return ApiResponse.success(adminConfigService.saveRole(null, payload));
    }

    @PutMapping("/api/admin/system/roles/{id}")
    @Operation(summary = "更新角色")
    public ApiResponse<AdminConfigDtos.RolePayload> updateRole(@PathVariable Long id,
                                                               @RequestBody AdminConfigDtos.RolePayload payload) {
        return ApiResponse.success(adminConfigService.saveRole(id, payload));
    }

    @DeleteMapping("/api/admin/system/roles/{id}")
    @Operation(summary = "删除角色")
    public ApiResponse<Boolean> deleteRole(@PathVariable Long id) {
        adminConfigService.deleteRole(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/system/menus")
    @Operation(summary = "查询菜单列表")
    public ApiResponse<List<AdminConfigDtos.MenuPayload>> menus() {
        return ApiResponse.success(adminConfigService.listMenus());
    }

    @GetMapping("/api/admin/system/menus/{id}")
    @Operation(summary = "查询菜单详情")
    public ApiResponse<AdminConfigDtos.MenuPayload> menu(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getMenu(id));
    }

    @PostMapping("/api/admin/system/menus")
    @Operation(summary = "新增菜单")
    public ApiResponse<AdminConfigDtos.MenuPayload> createMenu(@RequestBody AdminConfigDtos.MenuPayload payload) {
        return ApiResponse.success(adminConfigService.saveMenu(null, payload));
    }

    @PutMapping("/api/admin/system/menus/{id}")
    @Operation(summary = "更新菜单")
    public ApiResponse<AdminConfigDtos.MenuPayload> updateMenu(@PathVariable Long id,
                                                               @RequestBody AdminConfigDtos.MenuPayload payload) {
        return ApiResponse.success(adminConfigService.saveMenu(id, payload));
    }

    @DeleteMapping("/api/admin/system/menus/{id}")
    @Operation(summary = "删除菜单")
    public ApiResponse<Boolean> deleteMenu(@PathVariable Long id) {
        adminConfigService.deleteMenu(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/system/permissions")
    @Operation(summary = "查询权限点列表")
    public ApiResponse<List<AdminConfigDtos.PermissionPayload>> permissions() {
        return ApiResponse.success(adminConfigService.listPermissions());
    }

    @GetMapping("/api/admin/system/permissions/{id}")
    @Operation(summary = "查询权限点详情")
    public ApiResponse<AdminConfigDtos.PermissionPayload> permission(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getPermission(id));
    }

    @PostMapping("/api/admin/system/permissions")
    @Operation(summary = "新增权限点")
    public ApiResponse<AdminConfigDtos.PermissionPayload> createPermission(@RequestBody AdminConfigDtos.PermissionPayload payload) {
        return ApiResponse.success(adminConfigService.savePermission(null, payload));
    }

    @PutMapping("/api/admin/system/permissions/{id}")
    @Operation(summary = "更新权限点")
    public ApiResponse<AdminConfigDtos.PermissionPayload> updatePermission(@PathVariable Long id,
                                                                           @RequestBody AdminConfigDtos.PermissionPayload payload) {
        return ApiResponse.success(adminConfigService.savePermission(id, payload));
    }

    @DeleteMapping("/api/admin/system/permissions/{id}")
    @Operation(summary = "删除权限点")
    public ApiResponse<Boolean> deletePermission(@PathVariable Long id) {
        adminConfigService.deletePermission(id);
        return ApiResponse.success(true);
    }
}
