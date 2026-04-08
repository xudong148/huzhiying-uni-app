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
 * 后台营销与会员配置接口。
 */
@RestController
@Tag(name = "admin-marketing", description = "后台营销与会员配置")
public class AdminMarketingController {

    private final AdminConfigService adminConfigService;

    public AdminMarketingController(AdminConfigService adminConfigService) {
        this.adminConfigService = adminConfigService;
    }

    @GetMapping("/api/admin/marketing/coupons")
    @Operation(summary = "查询优惠券列表")
    public ApiResponse<List<AdminConfigDtos.CouponPayload>> coupons() {
        return ApiResponse.success(adminConfigService.listCoupons());
    }

    @GetMapping("/api/admin/marketing/coupons/{id}")
    @Operation(summary = "查询优惠券详情")
    public ApiResponse<AdminConfigDtos.CouponPayload> coupon(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getCoupon(id));
    }

    @PostMapping("/api/admin/marketing/coupons")
    @Operation(summary = "新增优惠券")
    public ApiResponse<AdminConfigDtos.CouponPayload> createCoupon(@RequestBody AdminConfigDtos.CouponPayload payload) {
        return ApiResponse.success(adminConfigService.saveCoupon(null, payload));
    }

    @PutMapping("/api/admin/marketing/coupons/{id}")
    @Operation(summary = "更新优惠券")
    public ApiResponse<AdminConfigDtos.CouponPayload> updateCoupon(@PathVariable("id") Long id,
                                                                   @RequestBody AdminConfigDtos.CouponPayload payload) {
        return ApiResponse.success(adminConfigService.saveCoupon(id, payload));
    }

    @DeleteMapping("/api/admin/marketing/coupons/{id}")
    @Operation(summary = "删除优惠券")
    public ApiResponse<Boolean> deleteCoupon(@PathVariable("id") Long id) {
        adminConfigService.deleteCoupon(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/marketing/member-levels")
    @Operation(summary = "查询会员等级列表")
    public ApiResponse<List<AdminConfigDtos.MemberLevelPayload>> memberLevels() {
        return ApiResponse.success(adminConfigService.listMemberLevels());
    }

    @GetMapping("/api/admin/marketing/member-levels/{id}")
    @Operation(summary = "查询会员等级详情")
    public ApiResponse<AdminConfigDtos.MemberLevelPayload> memberLevel(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getMemberLevel(id));
    }

    @PostMapping("/api/admin/marketing/member-levels")
    @Operation(summary = "新增会员等级")
    public ApiResponse<AdminConfigDtos.MemberLevelPayload> createMemberLevel(@RequestBody AdminConfigDtos.MemberLevelPayload payload) {
        return ApiResponse.success(adminConfigService.saveMemberLevel(null, payload));
    }

    @PutMapping("/api/admin/marketing/member-levels/{id}")
    @Operation(summary = "更新会员等级")
    public ApiResponse<AdminConfigDtos.MemberLevelPayload> updateMemberLevel(@PathVariable("id") Long id,
                                                                             @RequestBody AdminConfigDtos.MemberLevelPayload payload) {
        return ApiResponse.success(adminConfigService.saveMemberLevel(id, payload));
    }

    @DeleteMapping("/api/admin/marketing/member-levels/{id}")
    @Operation(summary = "删除会员等级")
    public ApiResponse<Boolean> deleteMemberLevel(@PathVariable("id") Long id) {
        adminConfigService.deleteMemberLevel(id);
        return ApiResponse.success(true);
    }
}
