package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台业务详情与动作接口 DTO。
 */
public final class AdminBusinessDtos {

    private AdminBusinessDtos() {
    }

    @Schema(name = "AdminTimelineItem", description = "后台订单时间轴节点")
    public record TimelineItem(
            @Schema(description = "步骤键", example = "dispatch")
            String stepKey,
            @Schema(description = "步骤标题", example = "等待派单")
            String label,
            @Schema(description = "步骤说明", example = "平台正在匹配附近师傅")
            String description,
            @Schema(description = "是否已完成", example = "true")
            boolean done,
            @Schema(description = "记录时间", example = "2026-04-08T15:30:00")
            LocalDateTime time
    ) {
    }

    @Schema(name = "AdminTrackPoint", description = "后台订单轨迹点")
    public record TrackPoint(
            @Schema(description = "轨迹点 ID", example = "1001")
            Long id,
            @Schema(description = "轨迹类型", example = "CHECK_IN")
            String type,
            @Schema(description = "轨迹标题", example = "到场签到")
            String label,
            @Schema(description = "轨迹说明", example = "师傅已到场并完成签到")
            String description,
            @Schema(description = "纬度", example = "31.2253")
            Double latitude,
            @Schema(description = "经度", example = "121.5443")
            Double longitude,
            @Schema(description = "记录时间", example = "2026-04-08T15:36:00")
            LocalDateTime time
    ) {
    }

    @Schema(name = "AdminMediaItem", description = "后台媒体资源摘要")
    public record MediaItem(
            @Schema(description = "文件 ID", example = "9001")
            Long id,
            @Schema(description = "业务类型", example = "after_work_media")
            String bizType,
            @Schema(description = "原始文件名", example = "after-work.jpg")
            String originalName,
            @Schema(description = "访问地址", example = "/uploads/after-work.jpg")
            String url
    ) {
    }

    @Schema(name = "AdminQuotationItemView", description = "后台报价明细")
    public record QuotationItemView(
            @Schema(description = "项目名称", example = "新增配件")
            String name,
            @Schema(description = "项目金额", example = "88.00")
            BigDecimal amount
    ) {
    }

    @Schema(name = "AdminQuotationView", description = "后台报价单详情")
    public record QuotationView(
            @Schema(description = "报价单号", example = "QT-12345678")
            String quotationId,
            @Schema(description = "报价状态", example = "PENDING_CONFIRM")
            String status,
            @Schema(description = "报价备注", example = "现场新增配件和工时费")
            String remark,
            @Schema(description = "报价总金额", example = "88.00")
            BigDecimal totalAmount,
            @ArraySchema(schema = @Schema(implementation = QuotationItemView.class))
            List<QuotationItemView> items
    ) {
    }

    @Schema(name = "AdminDispatchDetail", description = "后台调度详情")
    public record DispatchDetail(
            @Schema(description = "调度任务 ID", example = "DISP-001")
            String taskId,
            @Schema(description = "关联订单号", example = "SO20260408001")
            String orderId,
            @Schema(description = "订单标题", example = "空调不制冷上门维修")
            String title,
            @Schema(description = "订单类型", example = "SERVICE")
            String orderType,
            @Schema(description = "调度状态", example = "抢单中")
            String taskStatus,
            @Schema(description = "派单方式", example = "ROB")
            String dispatchMode,
            @Schema(description = "当前师傅用户 ID", example = "20001")
            Long masterUserId,
            @Schema(description = "当前师傅姓名", example = "张师傅")
            String masterName,
            @Schema(description = "服务地址", example = "上海市浦东新区张江高科技园区 88 号")
            String address,
            @Schema(description = "预约时间", example = "今天 14:00-16:00")
            String appointment,
            @Schema(description = "订单金额", example = "88.00")
            BigDecimal amount,
            @Schema(description = "服务区域", example = "浦东新区")
            String area,
            @ArraySchema(schema = @Schema(description = "标签"))
            List<String> tags,
            @ArraySchema(schema = @Schema(implementation = TimelineItem.class))
            List<TimelineItem> timeline
    ) {
    }

    @Schema(name = "AdminDispatchAssignRequest", description = "后台指派师傅请求")
    public record DispatchAssignRequest(
            @Schema(description = "目标师傅用户 ID", example = "20001")
            Long masterUserId,
            @Schema(description = "目标师傅姓名，和 masterUserId 二选一即可", example = "张师傅")
            String masterName
    ) {
    }

    @Schema(name = "AdminReasonRequest", description = "后台填写原因请求")
    public record ReasonRequest(
            @Schema(description = "原因或备注", example = "用户要求取消")
            String reason
    ) {
    }

    @Schema(name = "AdminOrderDetail", description = "后台订单详情")
    public record OrderDetail(
            @Schema(description = "订单号", example = "SO20260408001")
            String orderId,
            @Schema(description = "订单类型", example = "SERVICE")
            String orderType,
            @Schema(description = "订单标题", example = "空调不制冷上门维修")
            String title,
            @Schema(description = "业务状态", example = "待接单")
            String status,
            @Schema(description = "支付状态", example = "已预付")
            String paymentStatus,
            @Schema(description = "下单用户", example = "周女士")
            String userName,
            @Schema(description = "服务地址", example = "上海市浦东新区张江高科技园区 88 号")
            String address,
            @Schema(description = "预约时间", example = "今天 14:00-16:00")
            String appointment,
            @Schema(description = "ETA", example = "26 分钟")
            String eta,
            @Schema(description = "订单金额", example = "88.00")
            BigDecimal amount,
            @Schema(description = "安装工单号，仅商品单可能返回", example = "SO20260407009")
            String installServiceOrderId,
            @Schema(description = "是否允许取消", example = "true")
            boolean canCancel,
            @Schema(description = "是否允许退款", example = "true")
            boolean canRefund,
            @Schema(implementation = QuotationView.class)
            QuotationView quotation,
            @ArraySchema(schema = @Schema(implementation = TimelineItem.class))
            List<TimelineItem> timeline,
            @ArraySchema(schema = @Schema(implementation = TrackPoint.class))
            List<TrackPoint> trackPoints
    ) {
    }

    @Schema(name = "AdminMasterDetail", description = "后台师傅详情")
    public record MasterDetail(
            @Schema(description = "师傅用户 ID", example = "20001")
            Long userId,
            @Schema(description = "师傅姓名", example = "张师傅")
            String realName,
            @Schema(description = "手机号", example = "170****8899")
            String mobile,
            @ArraySchema(schema = @Schema(description = "技能标签"))
            List<String> skillTags,
            @ArraySchema(schema = @Schema(description = "服务区域"))
            List<String> serviceAreas,
            @Schema(description = "保证金", example = "3000.00")
            BigDecimal deposit,
            @Schema(description = "信用分", example = "98")
            Integer creditScore,
            @Schema(description = "是否在线", example = "true")
            boolean online,
            @Schema(description = "是否启用听单", example = "true")
            boolean listening,
            @Schema(description = "最大接单距离，单位 km", example = "20")
            Integer maxDistanceKm,
            @Schema(description = "是否启用隐私号", example = "true")
            boolean privacyNumber,
            @Schema(description = "是否启用该师傅", example = "true")
            boolean enabled
    ) {
    }

    @Schema(name = "AdminMasterUpdateRequest", description = "后台更新师傅资料请求")
    public record MasterUpdateRequest(
            @Schema(description = "师傅姓名", example = "张师傅")
            String realName,
            @Schema(description = "手机号", example = "170****8899")
            String mobile,
            @ArraySchema(schema = @Schema(description = "技能标签"))
            List<String> skillTags,
            @ArraySchema(schema = @Schema(description = "服务区域"))
            List<String> serviceAreas,
            @Schema(description = "保证金", example = "3000.00")
            BigDecimal deposit,
            @Schema(description = "是否在线", example = "true")
            Boolean online,
            @Schema(description = "是否启用听单", example = "true")
            Boolean listening,
            @Schema(description = "最大接单距离，单位 km", example = "20")
            Integer maxDistanceKm,
            @Schema(description = "是否启用隐私号", example = "true")
            Boolean privacyNumber,
            @Schema(description = "是否启用该师傅", example = "true")
            Boolean enabled
    ) {
    }

    @Schema(name = "AdminMasterCreditRequest", description = "后台调整师傅信用分请求")
    public record MasterCreditRequest(
            @Schema(description = "最新信用分", example = "99")
            @Min(0) @Max(100)
            Integer creditScore
    ) {
    }

    @Schema(name = "AdminFinanceDetail", description = "后台财务详情")
    public record FinanceDetail(
            @Schema(description = "账单号", example = "SETTLE-1")
            String billNo,
            @Schema(description = "账单类型", example = "师傅结算")
            String type,
            @Schema(description = "当前状态", example = "待结算")
            String status,
            @Schema(description = "金额", example = "268.00")
            BigDecimal amount,
            @Schema(description = "关联订单号", example = "SO20260406018")
            String orderId,
            @Schema(description = "账单标题", example = "SO20260406018 智能锁安装结算")
            String title,
            @Schema(description = "关联师傅", example = "张师傅")
            String masterName,
            @Schema(description = "业务时间", example = "今天 10:18")
            String transactionTime,
            @Schema(description = "审核备注", example = "财务已确认")
            String remark
    ) {
    }

    @Schema(name = "AdminFinanceApproveRequest", description = "后台财务审核请求")
    public record FinanceApproveRequest(
            @Schema(description = "审核备注", example = "财务审核通过")
            String remark
    ) {
    }

    @Schema(name = "AdminArbitrationDetail", description = "后台仲裁详情")
    public record ArbitrationDetail(
            @Schema(description = "仲裁单号", example = "ARB-001")
            String id,
            @Schema(description = "关联订单号", example = "SO20260407009")
            String orderId,
            @Schema(description = "订单标题", example = "智能锁标准安装")
            String orderTitle,
            @Schema(description = "仲裁原因", example = "乱收费")
            String reason,
            @Schema(description = "仲裁状态", example = "待裁决")
            String status,
            @Schema(description = "处理结果", example = "判定退还增项费用")
            String result,
            @Schema(description = "聊天消息数量", example = "3")
            int messageCount,
            @ArraySchema(schema = @Schema(implementation = TrackPoint.class))
            List<TrackPoint> trackPoints,
            @ArraySchema(schema = @Schema(implementation = MediaItem.class))
            List<MediaItem> mediaItems
    ) {
    }

    @Schema(name = "AdminArbitrationResolveRequest", description = "后台仲裁裁决请求")
    public record ArbitrationResolveRequest(
            @Schema(description = "裁决后的状态文案", example = "已裁决")
            @NotBlank
            String statusText,
            @Schema(description = "裁决结果说明", example = "判定退还增项费用")
            @NotBlank
            String resultText
    ) {
    }
}
