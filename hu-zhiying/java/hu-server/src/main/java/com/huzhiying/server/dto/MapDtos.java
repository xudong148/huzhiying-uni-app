package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 地图与轨迹相关 DTO。
 */
public final class MapDtos {

    private MapDtos() {}

    @Schema(description = "服务城市")
    public record ServiceCityPayload(
            @Schema(description = "区域主键 ID", example = "1") Long id,
            @Schema(description = "城市名称", example = "上海") String name,
            @Schema(description = "区县名称", example = "浦东新区") String district,
            @Schema(description = "是否热门城市", example = "true") boolean hot
    ) {}

    @Schema(description = "逆地理解析请求")
    public record RegeoRequest(
            @Schema(description = "纬度", example = "31.2253") Double latitude,
            @Schema(description = "经度", example = "121.5443") Double longitude
    ) {}

    @Schema(description = "逆地理解析结果")
    public record RegeoPayload(
            @Schema(description = "城市名称", example = "上海") String city,
            @Schema(description = "区县名称", example = "浦东新区") String district,
            @Schema(description = "详细地址", example = "上海市浦东新区张江高科技园区 88 号") String address,
            @Schema(description = "是否可服务", example = "true") boolean serviceable
    ) {}

    @Schema(description = "服务围栏校验请求")
    public record GeofenceCheckRequest(
            @Schema(description = "城市名称", example = "上海") String city,
            @Schema(description = "区县名称", example = "浦东新区") String district
    ) {}

    @Schema(description = "服务围栏校验结果")
    public record GeofenceCheckPayload(
            @Schema(description = "是否可服务", example = "true") boolean serviceable,
            @Schema(description = "命中的区域说明", example = "上海·浦东新区") String matchedZone
    ) {}

    @Schema(description = "ETA 查询请求")
    public record EtaRequest(
            @Schema(description = "订单 ID", example = "SO20260408001") String orderId,
            @Schema(description = "纬度", example = "31.2253") Double latitude,
            @Schema(description = "经度", example = "121.5443") Double longitude
    ) {}

    @Schema(description = "ETA 查询结果")
    public record EtaPayload(
            @Schema(description = "预计到达时间", example = "18 分钟") String eta,
            @Schema(description = "路程说明", example = "约 4.6 公里") String distance
    ) {}

    @Schema(description = "预约时段")
    public record TimeSlotPayload(
            @Schema(description = "时段值", example = "今天 14:00-16:00") String value
    ) {}
}
