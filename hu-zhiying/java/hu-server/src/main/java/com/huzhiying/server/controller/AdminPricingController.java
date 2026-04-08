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
 * 后台定价与服务区域配置接口。
 */
@RestController
@Tag(name = "admin-pricing", description = "后台定价规则和服务区域配置")
public class AdminPricingController {

    private final AdminConfigService adminConfigService;

    public AdminPricingController(AdminConfigService adminConfigService) {
        this.adminConfigService = adminConfigService;
    }

    @GetMapping("/api/admin/pricing/rules")
    @Operation(summary = "查询定价规则列表")
    public ApiResponse<List<AdminConfigDtos.PricingRulePayload>> pricingRules() {
        return ApiResponse.success(adminConfigService.listPricingRules());
    }

    @GetMapping("/api/admin/pricing/rules/{id}")
    @Operation(summary = "查询定价规则详情")
    public ApiResponse<AdminConfigDtos.PricingRulePayload> pricingRule(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getPricingRule(id));
    }

    @PostMapping("/api/admin/pricing/rules")
    @Operation(summary = "新增定价规则")
    public ApiResponse<AdminConfigDtos.PricingRulePayload> createPricingRule(@RequestBody AdminConfigDtos.PricingRulePayload payload) {
        return ApiResponse.success(adminConfigService.savePricingRule(null, payload));
    }

    @PutMapping("/api/admin/pricing/rules/{id}")
    @Operation(summary = "更新定价规则")
    public ApiResponse<AdminConfigDtos.PricingRulePayload> updatePricingRule(@PathVariable("id") Long id,
                                                                             @RequestBody AdminConfigDtos.PricingRulePayload payload) {
        return ApiResponse.success(adminConfigService.savePricingRule(id, payload));
    }

    @DeleteMapping("/api/admin/pricing/rules/{id}")
    @Operation(summary = "删除定价规则")
    public ApiResponse<Boolean> deletePricingRule(@PathVariable("id") Long id) {
        adminConfigService.deletePricingRule(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/dispatch/zones")
    @Operation(summary = "查询服务区域列表")
    public ApiResponse<List<AdminConfigDtos.DispatchZonePayload>> dispatchZones() {
        return ApiResponse.success(adminConfigService.listDispatchZones());
    }

    @GetMapping("/api/admin/dispatch/zones/{id}")
    @Operation(summary = "查询服务区域详情")
    public ApiResponse<AdminConfigDtos.DispatchZonePayload> dispatchZone(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getDispatchZone(id));
    }

    @PostMapping("/api/admin/dispatch/zones")
    @Operation(summary = "新增服务区域")
    public ApiResponse<AdminConfigDtos.DispatchZonePayload> createDispatchZone(@RequestBody AdminConfigDtos.DispatchZonePayload payload) {
        return ApiResponse.success(adminConfigService.saveDispatchZone(null, payload));
    }

    @PutMapping("/api/admin/dispatch/zones/{id}")
    @Operation(summary = "更新服务区域")
    public ApiResponse<AdminConfigDtos.DispatchZonePayload> updateDispatchZone(@PathVariable("id") Long id,
                                                                               @RequestBody AdminConfigDtos.DispatchZonePayload payload) {
        return ApiResponse.success(adminConfigService.saveDispatchZone(id, payload));
    }

    @DeleteMapping("/api/admin/dispatch/zones/{id}")
    @Operation(summary = "删除服务区域")
    public ApiResponse<Boolean> deleteDispatchZone(@PathVariable("id") Long id) {
        adminConfigService.deleteDispatchZone(id);
        return ApiResponse.success(true);
    }
}
