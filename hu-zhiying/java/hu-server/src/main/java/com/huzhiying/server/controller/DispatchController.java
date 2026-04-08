package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DispatchController {

    private final PlatformFacadeService platformFacadeService;

    public DispatchController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/dispatch/tasks")
    public ApiResponse<?> tasks() {
        return ApiResponse.success(platformFacadeService.dispatchTasks());
    }

    @PostMapping("/api/dispatch/tasks/{id}/claim")
    public ApiResponse<?> claim(@PathVariable String id, @RequestBody ClaimRequest request) {
        return ApiResponse.success(platformFacadeService.claimTask(id, request.masterName()));
    }

    @PostMapping("/api/dispatch/tasks/{id}/force-assign")
    public ApiResponse<?> forceAssign(@PathVariable String id, @RequestBody ClaimRequest request) {
        return ApiResponse.success("已强派", platformFacadeService.forceAssignTask(id, request.masterName()));
    }

    public record ClaimRequest(String masterName) {}
}
