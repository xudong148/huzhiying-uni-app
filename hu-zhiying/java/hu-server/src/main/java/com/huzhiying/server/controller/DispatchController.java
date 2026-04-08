package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "dispatch", description = "派单、抢单与强派接口")
public class DispatchController {

    private final PlatformFacadeService platformFacadeService;

    public DispatchController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/dispatch/tasks")
    @Operation(summary = "查询派单任务")
    public ApiResponse<?> tasks() {
        return ApiResponse.success(platformFacadeService.dispatchTasks());
    }

    @PostMapping("/api/dispatch/tasks/{id}/claim")
    @Operation(summary = "师傅抢单")
    public ApiResponse<?> claim(@PathVariable("id") String id, @RequestBody ClaimRequest request) {
        return ApiResponse.success(platformFacadeService.claimTask(id, request.masterName()));
    }

    @PostMapping("/api/dispatch/tasks/{id}/force-assign")
    @Operation(summary = "后台强派")
    public ApiResponse<?> forceAssign(@PathVariable("id") String id, @RequestBody ClaimRequest request) {
        return ApiResponse.success("已完成强派", platformFacadeService.forceAssignTask(id, request.masterName()));
    }

    public record ClaimRequest(String masterName) {}
}
