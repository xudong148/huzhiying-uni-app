package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "master", description = "师傅端工作台、聊天与设置接口")
public class MasterController {

    private final PlatformFacadeService platformFacadeService;

    public MasterController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/master/dashboard")
    @Operation(summary = "查询师傅工作台")
    public ApiResponse<?> dashboard() {
        return ApiResponse.success(platformFacadeService.masterDashboard());
    }

    @PostMapping("/api/master/apply")
    @Operation(summary = "提交师傅入驻申请")
    public ApiResponse<?> apply(@RequestBody MasterApplyRequest request) {
        return ApiResponse.success(platformFacadeService.applyMaster(
                request.realName(), request.mobile(), request.skills(), request.area()));
    }

    @GetMapping("/api/master/wallet")
    @Operation(summary = "查询师傅钱包")
    public ApiResponse<?> wallet() {
        return ApiResponse.success(java.util.Map.of(
                "account", platformFacadeService.wallet(),
                "transactions", platformFacadeService.walletTransactions()
        ));
    }

    @GetMapping("/api/master/settings")
    @Operation(summary = "查询师傅设置")
    public ApiResponse<?> settings() {
        return ApiResponse.success(platformFacadeService.masterSettings());
    }

    @PostMapping("/api/master/settings")
    @Operation(summary = "保存师傅设置")
    public ApiResponse<?> saveSettings(@RequestBody SaveSettingsRequest request) {
        return ApiResponse.success(platformFacadeService.saveMasterSettings(
                request.listening(),
                request.maxDistance(),
                request.privacyNumber()
        ));
    }

    @GetMapping("/api/messages/sessions")
    @Operation(summary = "查询聊天会话")
    public ApiResponse<?> sessions() {
        return ApiResponse.success(platformFacadeService.messageSessions());
    }

    @GetMapping("/api/messages/{sessionId}/items")
    @Operation(summary = "查询聊天消息")
    public ApiResponse<?> items(@PathVariable("sessionId") String sessionId) {
        return ApiResponse.success(platformFacadeService.messageItems(sessionId));
    }

    @PostMapping("/api/messages/{sessionId}/read")
    @Operation(summary = "回写会话已读状态")
    public ApiResponse<?> markRead(@PathVariable("sessionId") String sessionId) {
        return ApiResponse.success(platformFacadeService.markMessageSessionRead(sessionId));
    }

    @PostMapping("/api/messages/{sessionId}/items")
    @Operation(summary = "发送聊天消息")
    public ApiResponse<?> send(@PathVariable("sessionId") String sessionId, @RequestBody SupportDtos.SendMessageRequest request) {
        return ApiResponse.success(platformFacadeService.sendMessage(
                sessionId,
                request.senderCode(),
                request.messageType(),
                request.content()
        ));
    }

    @GetMapping("/api/notifications")
    @Operation(summary = "查询通知中心")
    public ApiResponse<?> notifications() {
        return ApiResponse.success(platformFacadeService.notifications());
    }

    @PostMapping("/api/master/orders/{id}/check-in")
    @Operation(summary = "师傅到场签到")
    public ApiResponse<?> checkIn(@PathVariable("id") String id, @RequestBody SupportDtos.CheckInRequest request) {
        return ApiResponse.success(platformFacadeService.masterCheckIn(
                id,
                request.latitude(),
                request.longitude(),
                request.accuracy()
        ));
    }

    @PostMapping("/api/master/orders/{id}/before-work-media")
    @Operation(summary = "上传施工前媒体")
    public ApiResponse<?> beforeWorkMedia(@PathVariable("id") String id, @RequestBody SupportDtos.BindMediaRequest request) {
        return ApiResponse.success(platformFacadeService.attachOrderMedia(id, "before_work_media", request.fileIds(), request.note()));
    }

    @PostMapping("/api/master/orders/{id}/after-work-media")
    @Operation(summary = "上传完工媒体")
    public ApiResponse<?> afterWorkMedia(@PathVariable("id") String id, @RequestBody SupportDtos.BindMediaRequest request) {
        return ApiResponse.success(platformFacadeService.attachOrderMedia(id, "after_work_media", request.fileIds(), request.note()));
    }

    public record MasterApplyRequest(String realName, String mobile, String skills, String area) {}

    public record SaveSettingsRequest(boolean listening, String maxDistance, boolean privacyNumber) {}
}
