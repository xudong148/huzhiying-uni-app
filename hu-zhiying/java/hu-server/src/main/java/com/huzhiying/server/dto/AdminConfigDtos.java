package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * 后台配置模块的请求和响应 DTO。
 */
public final class AdminConfigDtos {

    private AdminConfigDtos() {}

    @Schema(description = "Banner 配置")
    public record BannerPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "标题", example = "家电维修专场") String title,
            @Schema(description = "副标题", example = "不修不收费，90 天质保") String subtitle,
            @Schema(description = "图片地址", example = "https://cdn.example.com/banner/repair.jpg") String image,
            @Schema(description = "跳转链接", example = "/pages/goods/detail?id=201") String link,
            @Schema(description = "排序值", example = "10") Integer sortOrder,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "公告配置")
    public record NoticePayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "公告标题", example = "夜间服务费 22:00 后自动加收 30%") String title,
            @Schema(description = "公告内容", example = "具体收费以页面公示为准。") String content,
            @Schema(description = "级别", example = "warning") String levelCode,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "协议文档配置")
    public record AgreementPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "协议标题", example = "用户服务协议") String title,
            @Schema(description = "版本号", example = "2026.04") String version,
            @Schema(description = "正文内容", example = "这里是协议正文。") String content,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "优惠券配置")
    public record CouponPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "优惠券标题", example = "新人上门立减券") String title,
            @Schema(description = "面额", example = "30") BigDecimal amount,
            @Schema(description = "门槛说明", example = "满 99 减 30") String thresholdText,
            @Schema(description = "过期时间", example = "2026-04-30") String expireAt,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "会员等级配置")
    public record MemberLevelPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "等级名称", example = "SVIP 金卡") String name,
            @Schema(description = "权益说明", example = "服务 9 折 / 专属客服 / 优先派单") String benefitText,
            @Schema(description = "所需积分", example = "2000") Integer pointsRequired,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "角色配置")
    public record RolePayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "角色编码", example = "admin") String code,
            @Schema(description = "角色名称", example = "平台管理员") String name,
            @Schema(description = "描述", example = "拥有所有后台配置与审核权限") String description,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "菜单配置")
    public record MenuPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "菜单名称", example = "订单调度中心") String name,
            @Schema(description = "路由路径", example = "/dispatch") String path,
            @Schema(description = "图标", example = "radix-icons:dashboard") String icon,
            @Schema(description = "排序值", example = "20") Integer sortOrder,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "权限点配置")
    public record PermissionPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "权限编码", example = "order:force-assign") String code,
            @Schema(description = "权限名称", example = "订单强派") String name,
            @Schema(description = "描述", example = "允许管理员进行人工强派") String description,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "服务类目配置")
    public record ServiceCategoryPayload(
            @Schema(description = "主键 ID", example = "2") Long id,
            @Schema(description = "类目名称", example = "专业维修") String name,
            @Schema(description = "图标地址", example = "/static/icons/screwdriver.svg") String icon,
            @Schema(description = "排序值", example = "10") Integer sortOrder,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "服务项配置")
    public record ServiceItemPayload(
            @Schema(description = "主键 ID", example = "201") Long id,
            @Schema(description = "所属类目 ID", example = "2") Long categoryId,
            @Schema(description = "服务名称", example = "空调不制冷上门维修") String name,
            @Schema(description = "副标题", example = "基础检测 + 故障排查") String subtitle,
            @Schema(description = "基础价", example = "58") BigDecimal basePrice,
            @Schema(description = "上门费", example = "30") BigDecimal doorPrice,
            @Schema(description = "指导价", example = "58 - 299") String guidePrice,
            @Schema(description = "质保说明", example = "90 天平台质保") String warrantyText,
            @Schema(description = "保障说明", example = "不修不收费|实名认证|平台担保") String guaranteesText,
            @Schema(description = "标签说明", example = "空调维修|极速上门") String tagsText,
            @Schema(description = "图片地址，使用 | 分隔", example = "https://cdn.example.com/repair-1.jpg|https://cdn.example.com/repair-2.jpg") String imageUrls,
            @Schema(description = "流程步骤，使用 | 分隔", example = "提交工单|派单|上门检测|施工完工") String processSteps,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "商品配置")
    public record ProductPayload(
            @Schema(description = "主键 ID", example = "1001") Long id,
            @Schema(description = "商品名称", example = "智能锁 Pro 套装") String name,
            @Schema(description = "商品描述", example = "支持购买后自动生成安装工单") String descriptionText,
            @Schema(description = "商品价格", example = "1699") BigDecimal price,
            @Schema(description = "是否自动生成安装工单", example = "true") Boolean createInstallOrder,
            @Schema(description = "商品图片", example = "https://cdn.example.com/product-lock.jpg") String imageUrl,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "SKU 配置")
    public record SkuPayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "所属商品 ID", example = "1001") Long productId,
            @Schema(description = "SKU 名称", example = "雅黑标准款") String name,
            @Schema(description = "价格", example = "1699") BigDecimal price,
            @Schema(description = "库存", example = "36") Integer stock,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "定价规则配置")
    public record PricingRulePayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "类目 ID", example = "2") Long categoryId,
            @Schema(description = "规则名称", example = "空调维修夜间加价") String label,
            @Schema(description = "基础价", example = "58") BigDecimal basePrice,
            @Schema(description = "系数说明", example = "夜间 +30%，加急 +20%") String coefficient,
            @Schema(description = "指导价说明", example = "58 - 299") String guidePrice,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}

    @Schema(description = "服务区域配置")
    public record DispatchZonePayload(
            @Schema(description = "主键 ID", example = "1") Long id,
            @Schema(description = "城市名称", example = "上海") String cityName,
            @Schema(description = "区县名称", example = "浦东新区") String districtName,
            @Schema(description = "排序值", example = "10") Integer sortOrder,
            @Schema(description = "是否启用", example = "true") Boolean enabled
    ) {}
}
