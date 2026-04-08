package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.AdminBusinessDtos;
import com.huzhiying.server.service.AdminBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台业务详情与动作接口。
 */
@RestController
@Tag(name = "admin-business", description = "后台业务详情与动作接口")
public class AdminBusinessController {

    private final AdminBusinessService adminBusinessService;

    public AdminBusinessController(AdminBusinessService adminBusinessService) {
        this.adminBusinessService = adminBusinessService;
    }

    @GetMapping("/api/admin/dispatch/{taskId}")
    @Operation(summary = "查询调度任务详情")
    public ApiResponse<?> dispatchDetail(@PathVariable("taskId") String taskId) {
        return ApiResponse.success(adminBusinessService.dispatchDetail(taskId));
    }

    @PostMapping("/api/admin/dispatch/{taskId}/assign")
    @Operation(summary = "后台指派师傅")
    public ApiResponse<?> assignDispatch(@PathVariable("taskId") String taskId,
                                         @RequestBody(required = false) AdminBusinessDtos.DispatchAssignRequest request) {
        AdminBusinessDtos.DispatchAssignRequest payload = request == null
                ? new AdminBusinessDtos.DispatchAssignRequest(null, null)
                : request;
        return ApiResponse.success(adminBusinessService.assignTask(taskId, payload));
    }

    @PostMapping("/api/admin/dispatch/{taskId}/cancel-order")
    @Operation(summary = "后台从调度中心取消订单")
    public ApiResponse<?> cancelDispatchOrder(@PathVariable("taskId") String taskId,
                                              @RequestBody(required = false) AdminBusinessDtos.ReasonRequest request) {
        return ApiResponse.success(adminBusinessService.cancelDispatchOrder(taskId, request));
    }

    @GetMapping("/api/admin/orders/{orderId}")
    @Operation(summary = "查询订单详情")
    public ApiResponse<?> orderDetail(@PathVariable("orderId") String orderId) {
        return ApiResponse.success(adminBusinessService.orderDetail(orderId));
    }

    @PostMapping("/api/admin/orders/{orderId}/cancel")
    @Operation(summary = "后台取消订单")
    public ApiResponse<?> cancelOrder(@PathVariable("orderId") String orderId,
                                      @RequestBody(required = false) AdminBusinessDtos.ReasonRequest request) {
        return ApiResponse.success(adminBusinessService.cancelOrder(orderId, request));
    }

    @PostMapping("/api/admin/orders/{orderId}/refund")
    @Operation(summary = "后台发起或审核退款")
    public ApiResponse<?> refundOrder(@PathVariable("orderId") String orderId,
                                      @RequestBody(required = false) AdminBusinessDtos.ReasonRequest request) {
        return ApiResponse.success(adminBusinessService.refundOrder(orderId, request));
    }

    @GetMapping("/api/admin/masters/{userId}")
    @Operation(summary = "查询师傅详情")
    public ApiResponse<?> masterDetail(@PathVariable("userId") Long userId) {
        return ApiResponse.success(adminBusinessService.masterDetail(userId));
    }

    @PutMapping("/api/admin/masters/{userId}")
    @Operation(summary = "更新师傅资料")
    public ApiResponse<?> updateMaster(@PathVariable("userId") Long userId,
                                       @RequestBody AdminBusinessDtos.MasterUpdateRequest request) {
        return ApiResponse.success(adminBusinessService.updateMaster(userId, request));
    }

    @PostMapping("/api/admin/masters/{userId}/enable")
    @Operation(summary = "启用师傅")
    public ApiResponse<?> enableMaster(@PathVariable("userId") Long userId) {
        return ApiResponse.success(adminBusinessService.enableMaster(userId));
    }

    @PostMapping("/api/admin/masters/{userId}/disable")
    @Operation(summary = "停用师傅")
    public ApiResponse<?> disableMaster(@PathVariable("userId") Long userId) {
        return ApiResponse.success(adminBusinessService.disableMaster(userId));
    }

    @PostMapping("/api/admin/masters/{userId}/credit-score")
    @Operation(summary = "调整师傅信用分")
    public ApiResponse<?> updateCreditScore(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody AdminBusinessDtos.MasterCreditRequest request) {
        return ApiResponse.success(adminBusinessService.updateCreditScore(userId, request));
    }

    @GetMapping("/api/admin/finance/{billNo}")
    @Operation(summary = "查询财务单详情")
    public ApiResponse<?> financeDetail(@PathVariable("billNo") String billNo) {
        return ApiResponse.success(adminBusinessService.financeDetail(billNo));
    }

    @PostMapping("/api/admin/finance/{billNo}/approve")
    @Operation(summary = "审核财务单")
    public ApiResponse<?> approveFinance(@PathVariable("billNo") String billNo,
                                         @RequestBody(required = false) AdminBusinessDtos.FinanceApproveRequest request) {
        return ApiResponse.success(adminBusinessService.approveFinance(billNo, request));
    }

    @GetMapping("/api/admin/arbitrations/{id}")
    @Operation(summary = "查询仲裁详情")
    public ApiResponse<?> arbitrationDetail(@PathVariable("id") String id) {
        return ApiResponse.success(adminBusinessService.arbitrationDetail(id));
    }

    @PostMapping("/api/admin/arbitrations/{id}/resolve")
    @Operation(summary = "提交仲裁裁决")
    public ApiResponse<?> resolveArbitration(@PathVariable("id") String id,
                                             @Valid @RequestBody AdminBusinessDtos.ArbitrationResolveRequest request) {
        return ApiResponse.success(adminBusinessService.resolveArbitration(id, request));
    }
}
