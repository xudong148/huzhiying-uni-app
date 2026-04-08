package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支撑类接口 DTO。
 */
public final class SupportDtos {

    private SupportDtos() {}

    @Schema(name = "MediaFilePayload", description = "媒体文件元数据")
    public record MediaFilePayload(
            @Schema(description = "文件 ID", example = "9001")
            Long id,
            @Schema(description = "业务类型", example = "order_evidence")
            String bizType,
            @Schema(description = "业务主键", example = "SO20260408001")
            String bizId,
            @Schema(description = "原始文件名", example = "before-work.jpg")
            String originalName,
            @Schema(description = "文件访问地址", example = "/api/files/9001/content")
            String url,
            @Schema(description = "文件类型", example = "image/jpeg")
            String contentType,
            @Schema(description = "文件大小，单位字节", example = "204800")
            Long size,
            @Schema(description = "上传时间", example = "2026-04-08T11:30:00")
            LocalDateTime createdAt
    ) {}

    @Schema(name = "CommentPayload", description = "服务评价")
    public record CommentPayload(
            @Schema(description = "评价 ID", example = "1")
            Long id,
            @Schema(description = "评价用户", example = "周女士")
            String user,
            @Schema(description = "评分", example = "5")
            Integer score,
            @Schema(description = "评价内容", example = "师傅响应很快，现场报价也很清楚。")
            String content,
            @ArraySchema(schema = @Schema(description = "评价图片"))
            List<String> images,
            @ArraySchema(schema = @Schema(description = "评价标签"))
            List<String> tags,
            @Schema(description = "评价日期", example = "2026-04-06")
            String date
    ) {}

    @Schema(name = "OrderTrackPointPayload", description = "订单轨迹点")
    public record OrderTrackPointPayload(
            @Schema(description = "轨迹点 ID", example = "101")
            Long id,
            @Schema(description = "轨迹类型", example = "ARRIVED")
            String type,
            @Schema(description = "轨迹标题", example = "到达现场")
            String label,
            @Schema(description = "轨迹说明", example = "师傅已到达服务地址并完成签到。")
            String description,
            @Schema(description = "纬度", example = "31.2253")
            Double latitude,
            @Schema(description = "经度", example = "121.5443")
            Double longitude,
            @Schema(description = "时间", example = "2026-04-08T11:36:00")
            LocalDateTime time
    ) {}

    @Schema(name = "OrderTrackingPayload", description = "订单轨迹详情")
    public record OrderTrackingPayload(
            @Schema(description = "订单号", example = "SO20260408001")
            String orderId,
            @Schema(description = "状态文案", example = "师傅已到场")
            String statusText,
            @Schema(description = "预约时间", example = "今天 14:00-16:00")
            String appointment,
            @Schema(description = "服务地址", example = "上海市浦东新区张江路 188 号")
            String address,
            @Schema(description = "ETA", example = "12 分钟")
            String eta,
            @ArraySchema(schema = @Schema(implementation = OrderTrackPointPayload.class))
            List<OrderTrackPointPayload> points
    ) {}

    @Schema(name = "WechatPrepayPayload", description = "微信预支付参数")
    public record WechatPrepayPayload(
            @Schema(description = "订单号", example = "SO20260408001")
            String orderId,
            @Schema(description = "微信应用 ID", example = "wx1234567890abcdef")
            String appId,
            @Schema(description = "时间戳", example = "1712553600")
            String timeStamp,
            @Schema(description = "随机串", example = "f10c946ca14c4706abf414c0f8f10a1c")
            String nonceStr,
            @Schema(description = "微信支付 package 字段", example = "prepay_id=wx201410272009395522657a690389285100")
            String packageValue,
            @Schema(description = "签名算法", example = "RSA")
            String signType,
            @Schema(description = "支付签名", example = "MEUCIG9...")
            String paySign,
            @Schema(description = "是否已启用真实支付能力", example = "true")
            boolean payEnabled,
            @Schema(description = "提示文案", example = "已创建微信预支付单，请在微信客户端完成支付。")
            String message
    ) {}

    @Schema(name = "CancelOrderRequest", description = "取消订单请求")
    public record CancelOrderRequest(
            @Schema(description = "取消原因", example = "临时不需要了")
            String reason
    ) {}

    @Schema(name = "UrgeOrderRequest", description = "催单请求")
    public record UrgeOrderRequest(
            @Schema(description = "催单备注", example = "请尽快安排师傅联系我")
            String remark
    ) {}

    @Schema(name = "CheckInRequest", description = "到场签到请求")
    public record CheckInRequest(
            @Schema(description = "纬度", example = "31.2253")
            Double latitude,
            @Schema(description = "经度", example = "121.5443")
            Double longitude,
            @Schema(description = "定位精度，单位米", example = "18")
            Double accuracy
    ) {}

    @Schema(name = "BindMediaRequest", description = "绑定媒体请求")
    public record BindMediaRequest(
            @ArraySchema(schema = @Schema(description = "文件 ID"))
            List<Long> fileIds,
            @Schema(description = "补充说明", example = "施工前环境照片")
            String note
    ) {}

    @Schema(name = "SendMessageRequest", description = "发送消息请求")
    public record SendMessageRequest(
            @Schema(description = "发送方编码", example = "user")
            String senderCode,
            @Schema(description = "消息类型", example = "text")
            String messageType,
            @Schema(description = "消息内容，文本消息为文案，图片/语音消息为文件 URL", example = "请提前联系我")
            String content
    ) {}
}
