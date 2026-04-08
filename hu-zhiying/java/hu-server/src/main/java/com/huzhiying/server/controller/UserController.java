package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "user", description = "用户资料、地址与权益接口")
public class UserController {

    private final PlatformFacadeService platformFacadeService;

    public UserController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/users/me")
    @Operation(summary = "查询当前用户信息")
    public ApiResponse<?> me() {
        return ApiResponse.success(platformFacadeService.currentUser());
    }

    @PutMapping("/api/users/me")
    @Operation(summary = "更新用户资料")
    public ApiResponse<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        return ApiResponse.success(platformFacadeService.saveProfile(request.nickname(), request.mobile()));
    }

    @GetMapping("/api/addresses")
    @Operation(summary = "查询地址列表")
    public ApiResponse<?> addresses() {
        return ApiResponse.success(platformFacadeService.addresses());
    }

    @PostMapping("/api/addresses")
    @Operation(summary = "保存地址")
    public ApiResponse<?> saveAddress(@RequestBody SaveAddressRequest request) {
        return ApiResponse.success(platformFacadeService.saveAddress(
                request.id(),
                request.tag(),
                request.name(),
                request.mobile(),
                request.detailAddress(),
                request.isDefault()
        ));
    }

    @DeleteMapping("/api/addresses/{id}")
    @Operation(summary = "删除地址")
    public ApiResponse<?> deleteAddress(@PathVariable("id") Long id) {
        return ApiResponse.success(platformFacadeService.deleteAddress(id));
    }

    @GetMapping("/api/coupons")
    @Operation(summary = "查询优惠券列表")
    public ApiResponse<?> coupons() {
        return ApiResponse.success(platformFacadeService.coupons());
    }

    @GetMapping("/api/members/current")
    @Operation(summary = "查询当前会员等级")
    public ApiResponse<?> member() {
        return ApiResponse.success(platformFacadeService.currentMember());
    }

    public record UpdateProfileRequest(String nickname, String mobile) {}

    public record SaveAddressRequest(Long id, String tag, String name, String mobile, String detailAddress, boolean isDefault) {}
}
