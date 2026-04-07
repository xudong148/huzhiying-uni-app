package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MasterController {

    private final PlatformFacadeService platformFacadeService;

    public MasterController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/master/dashboard")
    public ApiResponse<?> dashboard() {
        return ApiResponse.success(platformFacadeService.masterDashboard());
    }

    @PostMapping("/api/master/apply")
    public ApiResponse<?> apply(@RequestBody MasterApplyRequest request) {
        return ApiResponse.success(platformFacadeService.applyMaster(
                request.realName(), request.mobile(), request.skills(), request.area()));
    }

    @GetMapping("/api/master/wallet")
    public ApiResponse<?> wallet() {
        return ApiResponse.success(
                java.util.Map.of(
                        "account", platformFacadeService.wallet(),
                        "transactions", platformFacadeService.walletTransactions()
                )
        );
    }

    @GetMapping("/api/master/settings")
    public ApiResponse<?> settings() {
        return ApiResponse.success(java.util.Map.of(
                "listening", true,
                "maxDistance", "20km",
                "privacyNumber", true
        ));
    }

    @GetMapping("/api/messages/sessions")
    public ApiResponse<?> sessions() {
        return ApiResponse.success(platformFacadeService.messageSessions());
    }

    @GetMapping("/api/messages/{sessionId}/items")
    public ApiResponse<?> items(@PathVariable String sessionId) {
        return ApiResponse.success(platformFacadeService.messageItems(sessionId));
    }

    @GetMapping("/api/notifications")
    public ApiResponse<?> notifications() {
        return ApiResponse.success(platformFacadeService.notices());
    }

    public record MasterApplyRequest(String realName, String mobile, String skills, String area) {}
}
