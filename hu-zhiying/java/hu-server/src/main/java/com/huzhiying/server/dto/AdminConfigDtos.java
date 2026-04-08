package com.huzhiying.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public final class AdminConfigDtos {

    private static final String LEVEL_CODE_PATTERN = "warning|promo|system";
    private static final String COUPON_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String ROLE_CODE_PATTERN = "^[a-z][a-z0-9_-]*$";
    private static final String MENU_PATH_PATTERN = "^/.*";
    private static final String PERMISSION_CODE_PATTERN = "^[a-z][a-z0-9_-]*:[a-z][a-z0-9_-]*$";

    private AdminConfigDtos() {}

    @Schema(description = "Banner 配置")
    public record BannerPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "Banner 标题不能为空")
            @Schema(description = "标题", example = "家电维修专场")
            String title,
            @Schema(description = "副标题", example = "不修不收费，90 天质保")
            String subtitle,
            @NotBlank(message = "Banner 图片不能为空")
            @Schema(description = "图片地址", example = "https://cdn.example.com/banner.jpg")
            String image,
            @NotBlank(message = "Banner 跳转链接不能为空")
            @Schema(description = "跳转链接", example = "/pages/goods/detail?id=201")
            String link,
            @NotNull(message = "Banner 排序不能为空")
            @PositiveOrZero(message = "Banner 排序不能小于 0")
            @Schema(description = "排序值", example = "10")
            Integer sortOrder,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "公告配置")
    public record NoticePayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "公告标题不能为空")
            @Schema(description = "标题", example = "夜间服务费 22:00 后自动加收 30%")
            String title,
            @NotBlank(message = "公告内容不能为空")
            @Schema(description = "内容", example = "具体收费以页面公示和现场检测为准。")
            String content,
            @NotBlank(message = "公告级别不能为空")
            @Pattern(regexp = LEVEL_CODE_PATTERN, message = "公告级别仅支持 warning、promo、system")
            @Schema(description = "级别", example = "warning")
            String levelCode,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "协议文档配置")
    public record AgreementPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "协议标题不能为空")
            @Schema(description = "标题", example = "用户服务协议")
            String title,
            @NotBlank(message = "协议版本不能为空")
            @Schema(description = "版本号", example = "2026.04")
            String version,
            @NotBlank(message = "协议正文不能为空")
            @Schema(description = "正文", example = "这里是协议正文")
            String content,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "优惠券配置")
    public record CouponPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "优惠券标题不能为空")
            @Schema(description = "标题", example = "新人上门立减券")
            String title,
            @NotNull(message = "优惠券面额不能为空")
            @DecimalMin(value = "0.00", message = "优惠券面额不能小于 0")
            @Schema(description = "面额", example = "30")
            BigDecimal amount,
            @NotBlank(message = "优惠券门槛说明不能为空")
            @Schema(description = "门槛说明", example = "满 99 减 30")
            String thresholdText,
            @NotBlank(message = "优惠券过期时间不能为空")
            @Pattern(regexp = COUPON_DATE_PATTERN, message = "优惠券过期时间必须是 yyyy-MM-dd")
            @Schema(description = "过期时间", example = "2026-04-30")
            String expireAt,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "会员等级配置")
    public record MemberLevelPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "会员等级名称不能为空")
            @Schema(description = "等级名称", example = "SVIP 金卡")
            String name,
            @NotBlank(message = "会员权益说明不能为空")
            @Schema(description = "权益说明", example = "服务 9 折 / 专属客服 / 优先派单")
            String benefitText,
            @NotNull(message = "会员等级积分门槛不能为空")
            @PositiveOrZero(message = "会员等级积分门槛不能小于 0")
            @Schema(description = "所需积分", example = "2000")
            Integer pointsRequired,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "角色配置")
    public record RolePayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "角色编码不能为空")
            @Pattern(regexp = ROLE_CODE_PATTERN, message = "角色编码仅支持小写字母、数字、- 和 _")
            @Schema(description = "角色编码", example = "admin")
            String code,
            @NotBlank(message = "角色名称不能为空")
            @Schema(description = "角色名称", example = "平台管理员")
            String name,
            @Schema(description = "描述", example = "拥有后台所有配置与审核权限")
            String description,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "菜单配置")
    public record MenuPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "菜单名称不能为空")
            @Schema(description = "菜单名称", example = "订单调度中心")
            String name,
            @NotBlank(message = "菜单路径不能为空")
            @Pattern(regexp = MENU_PATH_PATTERN, message = "菜单路径必须以 / 开头")
            @Schema(description = "菜单路径", example = "/dispatch")
            String path,
            @Schema(description = "图标", example = "mdi:view-dashboard")
            String icon,
            @NotNull(message = "菜单排序不能为空")
            @PositiveOrZero(message = "菜单排序不能小于 0")
            @Schema(description = "排序值", example = "20")
            Integer sortOrder,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "权限点配置")
    public record PermissionPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "权限编码不能为空")
            @Pattern(regexp = PERMISSION_CODE_PATTERN, message = "权限编码必须符合 module:action 格式")
            @Schema(description = "权限编码", example = "dispatch:force-assign")
            String code,
            @NotBlank(message = "权限名称不能为空")
            @Schema(description = "权限名称", example = "订单强派")
            String name,
            @Schema(description = "描述", example = "允许管理员执行强制派单")
            String description,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "角色菜单与权限绑定")
    public record RoleGrantPayload(
            @Schema(description = "角色 ID", example = "1")
            Long roleId,
            @Schema(description = "菜单 ID 列表", example = "[1,2,3]")
            List<Long> menuIds,
            @Schema(description = "权限 ID 列表", example = "[1,2]")
            List<Long> permissionIds
    ) {}

    @Schema(description = "服务类目配置")
    public record ServiceCategoryPayload(
            @Schema(description = "主键 ID", example = "2")
            Long id,
            @NotBlank(message = "服务类目名称不能为空")
            @Schema(description = "类目名称", example = "专业维修")
            String name,
            @Schema(description = "图标地址", example = "/static/icons/screwdriver.svg")
            String icon,
            @NotNull(message = "服务类目排序不能为空")
            @PositiveOrZero(message = "服务类目排序不能小于 0")
            @Schema(description = "排序值", example = "10")
            Integer sortOrder,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "服务项配置")
    public record ServiceItemPayload(
            @Schema(description = "主键 ID", example = "201")
            Long id,
            @NotNull(message = "服务项所属类目不能为空")
            @Schema(description = "类目 ID", example = "2")
            Long categoryId,
            @NotBlank(message = "服务项名称不能为空")
            @Schema(description = "服务名称", example = "空调上门维修")
            String name,
            @Schema(description = "副标题", example = "基础检测 + 故障排查")
            String subtitle,
            @NotNull(message = "服务项基础价不能为空")
            @DecimalMin(value = "0.00", message = "服务项基础价不能小于 0")
            @Schema(description = "基础价", example = "58")
            BigDecimal basePrice,
            @NotNull(message = "服务项上门费不能为空")
            @DecimalMin(value = "0.00", message = "服务项上门费不能小于 0")
            @Schema(description = "上门费", example = "30")
            BigDecimal doorPrice,
            @Schema(description = "指导价", example = "58 - 299")
            String guidePrice,
            @Schema(description = "质保说明", example = "90 天平台质保")
            String warrantyText,
            @Schema(description = "保障说明，使用 | 分隔", example = "不修不收费|实名认证|平台担保")
            String guaranteesText,
            @Schema(description = "标签说明，使用 | 分隔", example = "空调维修|极速上门")
            String tagsText,
            @Schema(description = "图片地址，使用 | 分隔", example = "https://cdn.example.com/a.jpg|https://cdn.example.com/b.jpg")
            String imageUrls,
            @Schema(description = "流程步骤，使用 | 分隔", example = "提交工单|派单|上门检测|施工完工")
            String processSteps,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "商品配置")
    public record ProductPayload(
            @Schema(description = "主键 ID", example = "1001")
            Long id,
            @NotBlank(message = "商品名称不能为空")
            @Schema(description = "商品名称", example = "智能锁 Pro 套装")
            String name,
            @Schema(description = "商品描述", example = "购买后自动生成安装工单")
            String descriptionText,
            @NotNull(message = "商品价格不能为空")
            @DecimalMin(value = "0.00", message = "商品价格不能小于 0")
            @Schema(description = "成交价", example = "1699")
            BigDecimal price,
            @DecimalMin(value = "0.00", message = "商品标签价不能小于 0")
            @Schema(description = "标签价", example = "1999")
            BigDecimal tagPrice,
            @DecimalMin(value = "0.00", message = "商品折扣价不能小于 0")
            @Schema(description = "折扣价", example = "1699")
            BigDecimal discountPrice,
            @Schema(description = "是否自动生成安装工单", example = "true")
            Boolean createInstallOrder,
            @Schema(description = "商品图片", example = "https://cdn.example.com/product-lock.jpg")
            String imageUrl,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "SKU 配置")
    public record SkuPayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotNull(message = "SKU 所属商品不能为空")
            @Schema(description = "商品 ID", example = "1001")
            Long productId,
            @NotBlank(message = "SKU 名称不能为空")
            @Schema(description = "SKU 名称", example = "雅黑标准款")
            String name,
            @NotNull(message = "SKU 价格不能为空")
            @DecimalMin(value = "0.00", message = "SKU 价格不能小于 0")
            @Schema(description = "成交价", example = "1699")
            BigDecimal price,
            @DecimalMin(value = "0.00", message = "SKU 标签价不能小于 0")
            @Schema(description = "标签价", example = "1999")
            BigDecimal tagPrice,
            @DecimalMin(value = "0.00", message = "SKU 折扣价不能小于 0")
            @Schema(description = "折扣价", example = "1699")
            BigDecimal discountPrice,
            @NotNull(message = "SKU 库存不能为空")
            @PositiveOrZero(message = "SKU 库存不能小于 0")
            @Schema(description = "库存", example = "36")
            Integer stock,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "定价规则配置")
    public record PricingRulePayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotNull(message = "定价规则所属类目不能为空")
            @Schema(description = "类目 ID", example = "2")
            Long categoryId,
            @NotBlank(message = "定价规则名称不能为空")
            @Schema(description = "规则名称", example = "空调维修夜间加价")
            String label,
            @NotNull(message = "定价规则基础价不能为空")
            @DecimalMin(value = "0.00", message = "定价规则基础价不能小于 0")
            @Schema(description = "基础价", example = "58")
            BigDecimal basePrice,
            @Schema(description = "系数说明", example = "夜间 +30%，加急 +20%")
            String coefficient,
            @Schema(description = "指导价说明", example = "58 - 299")
            String guidePrice,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}

    @Schema(description = "服务区域配置")
    public record DispatchZonePayload(
            @Schema(description = "主键 ID", example = "1")
            Long id,
            @NotBlank(message = "服务区域城市名称不能为空")
            @Schema(description = "城市名称", example = "上海")
            String cityName,
            @NotBlank(message = "服务区域区县名称不能为空")
            @Schema(description = "区县名称", example = "浦东新区")
            String districtName,
            @NotNull(message = "服务区域排序不能为空")
            @PositiveOrZero(message = "服务区域排序不能小于 0")
            @Schema(description = "排序值", example = "10")
            Integer sortOrder,
            @Schema(description = "是否启用", example = "true")
            Boolean enabled
    ) {}
}
