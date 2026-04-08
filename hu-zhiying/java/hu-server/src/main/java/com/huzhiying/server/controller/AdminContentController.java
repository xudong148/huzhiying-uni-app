package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.AdminConfigDtos;
import com.huzhiying.server.service.AdminConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台内容运营配置接口。
 */
@RestController
@Tag(name = "admin-content", description = "后台内容运营配置")
public class AdminContentController extends AdminConfigControllerSupport {

    private final AdminConfigService adminConfigService;

    public AdminContentController(AdminConfigService adminConfigService) {
        this.adminConfigService = adminConfigService;
    }

    @GetMapping("/api/admin/content/banners")
    @Operation(summary = "查询 Banner 列表")
    public ApiResponse<List<AdminConfigDtos.BannerPayload>> banners() {
        return ApiResponse.success(adminConfigService.listBanners());
    }

    @GetMapping("/api/admin/content/banners/{id}")
    @Operation(summary = "查询 Banner 详情")
    public ApiResponse<AdminConfigDtos.BannerPayload> banner(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getBanner(id));
    }

    @PostMapping("/api/admin/content/banners")
    @Operation(summary = "新增 Banner")
    public ApiResponse<AdminConfigDtos.BannerPayload> createBanner(@Valid @RequestBody AdminConfigDtos.BannerPayload payload) {
        return ApiResponse.success(adminConfigService.saveBanner(null, payload));
    }

    @PutMapping("/api/admin/content/banners/{id}")
    @Operation(summary = "更新 Banner")
    public ApiResponse<AdminConfigDtos.BannerPayload> updateBanner(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody AdminConfigDtos.BannerPayload payload) {
        return ApiResponse.success(adminConfigService.saveBanner(id, payload));
    }

    @DeleteMapping("/api/admin/content/banners/{id}")
    @Operation(summary = "删除 Banner")
    public ApiResponse<Boolean> deleteBanner(@PathVariable("id") Long id) {
        adminConfigService.deleteBanner(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/notices")
    @Operation(summary = "查询公告列表")
    public ApiResponse<List<AdminConfigDtos.NoticePayload>> notices() {
        return ApiResponse.success(adminConfigService.listNotices());
    }

    @GetMapping("/api/admin/content/notices/{id}")
    @Operation(summary = "查询公告详情")
    public ApiResponse<AdminConfigDtos.NoticePayload> notice(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getNotice(id));
    }

    @PostMapping("/api/admin/content/notices")
    @Operation(summary = "新增公告")
    public ApiResponse<AdminConfigDtos.NoticePayload> createNotice(@Valid @RequestBody AdminConfigDtos.NoticePayload payload) {
        return ApiResponse.success(adminConfigService.saveNotice(null, payload));
    }

    @PutMapping("/api/admin/content/notices/{id}")
    @Operation(summary = "更新公告")
    public ApiResponse<AdminConfigDtos.NoticePayload> updateNotice(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody AdminConfigDtos.NoticePayload payload) {
        return ApiResponse.success(adminConfigService.saveNotice(id, payload));
    }

    @DeleteMapping("/api/admin/content/notices/{id}")
    @Operation(summary = "删除公告")
    public ApiResponse<Boolean> deleteNotice(@PathVariable("id") Long id) {
        adminConfigService.deleteNotice(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/agreements")
    @Operation(summary = "查询协议列表")
    public ApiResponse<List<AdminConfigDtos.AgreementPayload>> agreements() {
        return ApiResponse.success(adminConfigService.listAgreements());
    }

    @GetMapping("/api/admin/content/agreements/{id}")
    @Operation(summary = "查询协议详情")
    public ApiResponse<AdminConfigDtos.AgreementPayload> agreement(@PathVariable("id") Long id) {
        return ApiResponse.success(adminConfigService.getAgreement(id));
    }

    @PostMapping("/api/admin/content/agreements")
    @Operation(summary = "新增协议")
    public ApiResponse<AdminConfigDtos.AgreementPayload> createAgreement(@Valid @RequestBody AdminConfigDtos.AgreementPayload payload) {
        return ApiResponse.success(adminConfigService.saveAgreement(null, payload));
    }

    @PutMapping("/api/admin/content/agreements/{id}")
    @Operation(summary = "更新协议")
    public ApiResponse<AdminConfigDtos.AgreementPayload> updateAgreement(@PathVariable("id") Long id,
                                                                         @Valid @RequestBody AdminConfigDtos.AgreementPayload payload) {
        return ApiResponse.success(adminConfigService.saveAgreement(id, payload));
    }

    @DeleteMapping("/api/admin/content/agreements/{id}")
    @Operation(summary = "删除协议")
    public ApiResponse<Boolean> deleteAgreement(@PathVariable("id") Long id) {
        adminConfigService.deleteAgreement(id);
        return ApiResponse.success(true);
    }
}
