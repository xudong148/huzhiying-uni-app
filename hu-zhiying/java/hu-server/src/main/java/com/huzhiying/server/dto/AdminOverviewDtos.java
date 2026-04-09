package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public final class AdminOverviewDtos {

    private AdminOverviewDtos() {
    }

    @Schema(name = "AdminFinanceRow", description = "Admin finance row")
    public record FinanceRow(
            @Schema(description = "Bill number", example = "SETTLE-12")
            String billNo,
            @Schema(description = "Finance type", example = "甯堝倕缁撶畻")
            String type,
            @Schema(description = "Current status", example = "待结算")
            String status,
            @Schema(description = "Amount", example = "268.00")
            BigDecimal amount,
            @Schema(description = "Related order id", example = "SO20260406018")
            String orderId,
            @Schema(description = "Title", example = "SO20260406018 鏅鸿兘閿佸畨瑁呯粨绠?")
            String title,
            @Schema(description = "Master name", example = "鏈嶅姟鎶€甯?")
            String masterName,
            @Schema(description = "Occurred time", example = "2026-04-09T12:00:00")
            String occurredAt,
            @Schema(description = "Business number", example = "STL-1712664000000")
            String bizNo
    ) {
    }

    @Schema(name = "AdminNotificationTaskItem", description = "Admin notification task row")
    public record NotificationTaskItem(
            @Schema(description = "Task id", example = "1")
            Long id,
            @Schema(description = "Business number", example = "NTF-1712664000000-a1b2c3")
            String bizNo,
            @Schema(description = "Business type", example = "SERVICE")
            String bizType,
            @Schema(description = "Business id", example = "SO20260406018")
            String bizId,
            @Schema(description = "Target role", example = "MASTER")
            String targetRole,
            @Schema(description = "Target user id", example = "20001")
            Long targetUserId,
            @Schema(description = "Channel", example = "PLATFORM")
            String channel,
            @Schema(description = "Template code", example = "settlement-created")
            String templateCode,
            @Schema(description = "Status", example = "PENDING")
            String status,
            @Schema(description = "Trace id", example = "notify-a1b2c3d4")
            String traceId,
            @Schema(description = "Created time", example = "2026-04-09T12:00:00")
            String createdAt,
            @Schema(description = "Updated time", example = "2026-04-09T12:05:00")
            String updatedAt,
            @Schema(description = "Sent time", example = "2026-04-09T12:05:00")
            String sentAt
    ) {
    }

    @Schema(name = "AdminNotificationDispatchResult", description = "Notification dispatch batch result")
    public record NotificationDispatchResult(
            @Schema(description = "Requested limit", example = "20")
            int requestedLimit,
            @Schema(description = "Processed task count", example = "2")
            int processedCount,
            @Schema(description = "Success count", example = "1")
            int successCount,
            @Schema(description = "Failed count", example = "1")
            int failedCount
    ) {
    }

    @Schema(name = "AdminNotificationTaskList", description = "Admin notification task list")
    public record NotificationTaskList(
            @ArraySchema(schema = @Schema(implementation = NotificationTaskItem.class))
            java.util.List<NotificationTaskItem> items
    ) {
    }
}
