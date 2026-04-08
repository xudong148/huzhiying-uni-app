package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用支持类 DTO，供首页、订单跟踪、文件、支付和消息接口复用。
 */
public final class SupportDtos {

    private SupportDtos() {
    }

    @Schema(name = "MediaFile", description = "平台存储的媒体文件元数据")
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
    ) {
    }

    @Schema(name = "CommentPayload", description = "服务评价")
    public record CommentPayload(
            @Schema(description = "评论 ID", example = "1")
            Long id,
            @Schema(description = "评论用户", example = "平台用户")
            String user,
            @Schema(description = "评分", example = "5")
            Integer score,
            @Schema(description = "评论内容", example = "师傅响应很快，报价也很清楚。")
            String content,
            @ArraySchema(schema = @Schema(description = "评论图片"))
            List<String> images,
            @ArraySchema(schema = @Schema(description = "评论标签"))
            List<String> tags,
            @Schema(description = "评论日期", example = "2026-04-06")
            String date
    ) {
    }

    @Schema(name = "OrderTrackPoint", description = "订单执行过程中的轨迹节点")
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
    ) {
    }

    @Schema(name = "TimelineItem", description = "订单流程时间轴")
    public record TimelineItemPayload(
            @Schema(description = "步骤编码", example = "dispatch")
            String key,
            @Schema(description = "步骤标题", example = "等待派单")
            String label,
            @Schema(description = "步骤说明", example = "平台正在匹配附近师傅")
            String desc,
            @Schema(description = "是否完成", example = "true")
            boolean done,
            @Schema(description = "记录时间", example = "2026-04-08T10:30:00")
            LocalDateTime time
    ) {
    }

    @Schema(name = "OrderTrackingPayload", description = "订单跟踪页使用的聚合数据")
    public record OrderTrackingPayload(
            @Schema(description = "订单号", example = "SO20260408001")
            String orderId,
            @Schema(description = "状态文案", example = "师傅已到场")
            String statusText,
            @Schema(description = "预约时间", example = "今天 14:00-16:00")
            String appointment,
            @Schema(description = "服务地址", example = "上海市浦东新区张江路 188 号")
            String address,
            @Schema(description = "预计到达时间", example = "12 分钟")
            String eta,
            @Schema(description = "距离说明", example = "约 4.6 公里")
            String distance,
            @ArraySchema(schema = @Schema(implementation = OrderTrackPointPayload.class))
            List<OrderTrackPointPayload> points,
            @ArraySchema(schema = @Schema(implementation = MediaFilePayload.class))
            List<MediaFilePayload> mediaFiles
    ) {
    }

    @Schema(name = "QuotationItemPayload", description = "报价明细项")
    public record QuotationItemPayload(
            @Schema(description = "项目名称", example = "新增配件")
            String name,
            @Schema(description = "项目金额", example = "88.00")
            BigDecimal amount
    ) {
    }

    @Schema(name = "QuotationPayload", description = "服务订单报价单")
    public record QuotationPayload(
            @Schema(description = "报价单号", example = "QT-12345678")
            String id,
            @Schema(description = "报价状态", example = "PENDING_CONFIRM")
            String status,
            @Schema(description = "备注", example = "现场新增辅材和工时费")
            String remark,
            @Schema(description = "总金额", example = "88.00")
            BigDecimal totalAmount,
            @ArraySchema(schema = @Schema(implementation = QuotationItemPayload.class))
            List<QuotationItemPayload> items
    ) {
    }

    @Schema(name = "MessageSummaryPayload", description = "订单关联的会话摘要")
    public record MessageSummaryPayload(
            @Schema(description = "会话 ID", example = "MS-001")
            String sessionId,
            @Schema(description = "会话标题", example = "空调维修沟通")
            String title,
            @Schema(description = "参与方", example = "订单用户")
            String participant,
            @Schema(description = "消息总数", example = "6")
            int messageCount,
            @Schema(description = "最后一条消息", example = "师傅 30 分钟内到达")
            String latestMessage
    ) {
    }

    @Schema(name = "OrderDetailPayload", description = "移动端订单详情聚合结构")
    public record OrderDetailPayload(
            @Schema(description = "订单号", example = "SO20260408001")
            String id,
            @Schema(description = "订单类型", example = "service")
            String type,
            @Schema(description = "订单标题", example = "空调上门维修")
            String title,
            @Schema(description = "业务状态", example = "PENDING_ACCEPT")
            String status,
            @Schema(description = "支付状态", example = "PARTIAL_PAID")
            String paymentStatus,
            @Schema(description = "状态文案", example = "等待师傅确认上门")
            String statusText,
            @Schema(description = "下单用户", example = "订单用户")
            String userName,
            @Schema(description = "服务师傅", example = "服务技师")
            String masterName,
            @Schema(description = "服务地址", example = "上海市浦东新区张江路 188 号")
            String address,
            @Schema(description = "预约时间", example = "今天 14:00-16:00")
            String appointment,
            @Schema(description = "预计到达时间", example = "26 分钟")
            String eta,
            @Schema(description = "订单金额", example = "88.00")
            BigDecimal amount,
            @Schema(description = "安装工单号，仅商品单可能返回", example = "SO20260407009")
            String installServiceOrderId,
            @Schema(description = "服务师傅手机号脱敏展示", example = "139****1234")
            String masterMobile,
            @Schema(description = "是否允许继续支付", example = "true")
            boolean canPay,
            @Schema(description = "是否允许催单", example = "true")
            boolean canUrge,
            @Schema(description = "是否允许取消", example = "true")
            boolean canCancel,
            @Schema(description = "是否允许售后", example = "true")
            boolean canRefund,
            @ArraySchema(schema = @Schema(implementation = TimelineItemPayload.class))
            List<TimelineItemPayload> timeline,
            @ArraySchema(schema = @Schema(implementation = MediaFilePayload.class))
            List<MediaFilePayload> mediaFiles,
            @Schema(implementation = QuotationPayload.class)
            QuotationPayload quotation,
            @Schema(implementation = MessageSummaryPayload.class)
            MessageSummaryPayload messageSummary
    ) {
    }

    @Schema(name = "WechatPrepayPayload", description = "微信预支付返回值")
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
    ) {
    }

    @Schema(name = "CancelOrderRequest", description = "取消订单表单")
    public record CancelOrderRequest(
            @Schema(description = "取消原因", example = "临时不需要了")
            String reason
    ) {
    }

    @Schema(name = "UrgeOrderRequest", description = "催单表单")
    public record UrgeOrderRequest(
            @Schema(description = "催单备注", example = "请尽快安排师傅联系我")
            String remark
    ) {
    }

    @Schema(name = "CheckInRequest", description = "师傅到场签到表单")
    public record CheckInRequest(
            @Schema(description = "纬度", example = "31.2253")
            Double latitude,
            @Schema(description = "经度", example = "121.5443")
            Double longitude,
            @Schema(description = "定位精度，单位米", example = "18")
            Double accuracy
    ) {
    }

    @Schema(name = "BindMediaRequest", description = "订单媒体绑定表单")
    public record BindMediaRequest(
            @ArraySchema(schema = @Schema(description = "文件 ID"))
            List<Long> fileIds,
            @Schema(description = "补充说明", example = "施工前环境照片")
            String note
    ) {
    }

    @Schema(name = "SendMessageRequest", description = "发送消息表单")
    public record SendMessageRequest(
            @Schema(description = "发送方编码", example = "user")
            String senderCode,
            @Schema(description = "消息类型", example = "text")
            String messageType,
            @Schema(description = "消息内容，文本消息为正文，图片或语音消息为文件 URL", example = "请提前联系我")
            String content
    ) {
    }

    @Schema(name = "HomeCategoryNavPayload", description = "首页分类导航项")
    public record HomeCategoryNavPayload(
            @Schema(description = "分类 ID", example = "2")
            Long id,
            @Schema(description = "分类名称", example = "专业维修")
            String name,
            @Schema(description = "图标地址", example = "/static/icons/screwdriver.svg")
            String icon,
            @ArraySchema(schema = @Schema(description = "服务子项名称"))
            List<String> subs
    ) {
    }

    @Schema(name = "EcosystemCardPayload", description = "首页生态入口卡片")
    public record EcosystemCardPayload(
            @Schema(description = "卡片 ID", example = "1")
            Long id,
            @Schema(description = "卡片名称", example = "小应学堂")
            String name,
            @Schema(description = "卡片描述", example = "师傅培训与故障案例库")
            String desc,
            @Schema(description = "本地图标", example = "/static/icons/school.svg")
            String icon,
            @Schema(description = "主题色", example = "#2B5CFF")
            String color
    ) {
    }

    @Schema(name = "RecommendationPayload", description = "首页推荐卡片")
    public record RecommendationPayload(
            @Schema(description = "业务主键", example = "201")
            Long id,
            @Schema(description = "推荐类型", example = "service")
            String type,
            @Schema(description = "标题", example = "空调上门维修")
            String title,
            @Schema(description = "标签", example = "上门服务")
            String tag,
            @Schema(description = "价格", example = "58")
            BigDecimal price,
            @Schema(description = "销量文案", example = "1.2k")
            String sales,
            @Schema(description = "封面图", example = "/static/icons/screwdriver.svg")
            String image
    ) {
    }

    @Schema(name = "SvipCardPayload", description = "首页 SVIP 卡片")
    public record SvipCardPayload(
            @Schema(description = "标题", example = "SVIP 金管家")
            String title,
            @Schema(description = "副标题", example = "专属客服、优先派单、年度保养")
            String subtitle,
            @ArraySchema(schema = @Schema(description = "权益标签"))
            List<String> tags
    ) {
    }

    @Schema(name = "HomePayload", description = "首页一次性读取的聚合内容")
    public record HomePayload(
            @Schema(description = "当前位置城市", example = "上海")
            String location,
            @ArraySchema(schema = @Schema(implementation = MapDtos.ServiceCityPayload.class))
            List<MapDtos.ServiceCityPayload> serviceCities,
            @ArraySchema(schema = @Schema(description = "热搜词"))
            List<String> hotKeywords,
            @ArraySchema(schema = @Schema(implementation = com.huzhiying.domain.model.DomainModels.Banner.class))
            List<com.huzhiying.domain.model.DomainModels.Banner> banners,
            @ArraySchema(schema = @Schema(implementation = com.huzhiying.domain.model.DomainModels.Notice.class))
            List<com.huzhiying.domain.model.DomainModels.Notice> notices,
            @ArraySchema(schema = @Schema(implementation = HomeCategoryNavPayload.class))
            List<HomeCategoryNavPayload> categoryNav,
            @Schema(implementation = SvipCardPayload.class)
            SvipCardPayload svipCard,
            @ArraySchema(schema = @Schema(implementation = EcosystemCardPayload.class))
            List<EcosystemCardPayload> ecosystemCards,
            @ArraySchema(schema = @Schema(implementation = RecommendationPayload.class))
            List<RecommendationPayload> recommendations
    ) {
    }
}
