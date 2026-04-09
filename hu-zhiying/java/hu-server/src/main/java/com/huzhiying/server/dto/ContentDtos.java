package com.huzhiying.server.dto;

import com.huzhiying.domain.model.DomainModels;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页内容、学堂和圈子的独立 DTO，避免继续挤在 SupportDtos 中。
 */
public final class ContentDtos {

    private ContentDtos() {
    }

    @Schema(name = "HomeCategoryNavPayload", description = "首页分类导航")
    public record HomeCategoryNavPayload(
            @Schema(description = "分类 ID", example = "2")
            Long id,
            @Schema(description = "分类名称", example = "专业维修")
            String name,
            @Schema(description = "图标地址", example = "/static/icons/screwdriver.svg")
            String icon,
            @ArraySchema(schema = @Schema(description = "子项名称"))
            List<String> subs
    ) {
    }

    @Schema(name = "EcosystemCardPayload", description = "首页生态卡片")
    public record EcosystemCardPayload(
            @Schema(description = "卡片 ID", example = "1")
            Long id,
            @Schema(description = "卡片名称", example = "小应学堂")
            String name,
            @Schema(description = "卡片描述", example = "金牌技师进阶课")
            String desc,
            @Schema(description = "图标", example = "https://api.iconify.design/fluent-emoji-flat:graduation-cap.svg")
            String icon,
            @Schema(description = "主题色", example = "#2B5CFF")
            String color,
            @Schema(description = "真实路由", example = "/pages/academy/index")
            String link
    ) {
    }

    @Schema(name = "RecommendationPayload", description = "首页推荐卡片")
    public record RecommendationPayload(
            @Schema(description = "业务主键", example = "201")
            Long id,
            @Schema(description = "类型", example = "service")
            String type,
            @Schema(description = "标题", example = "空调上门维修")
            String title,
            @Schema(description = "标签", example = "上门服务")
            String tag,
            @Schema(description = "价格", example = "58")
            java.math.BigDecimal price,
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

    @Schema(name = "HomePayload", description = "首页聚合内容")
    public record HomePayload(
            @Schema(description = "当前位置城市", example = "上海")
            String location,
            @ArraySchema(schema = @Schema(implementation = MapDtos.ServiceCityPayload.class))
            List<MapDtos.ServiceCityPayload> serviceCities,
            @ArraySchema(schema = @Schema(description = "热搜词"))
            List<String> hotKeywords,
            @ArraySchema(schema = @Schema(implementation = DomainModels.Banner.class))
            List<DomainModels.Banner> banners,
            @ArraySchema(schema = @Schema(implementation = DomainModels.Notice.class))
            List<DomainModels.Notice> notices,
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

    @Schema(name = "AcademyCategoryPayload", description = "学堂栏目")
    public record AcademyCategoryPayload(
            @Schema(description = "栏目 ID", example = "1")
            Long id,
            @Schema(description = "栏目名称", example = "维修入门")
            String name,
            @Schema(description = "图标", example = "/static/icons/school.svg")
            String icon,
            @Schema(description = "描述", example = "常见故障与入门知识")
            String description,
            @Schema(description = "是否启用", example = "true")
            boolean enabled,
            @Schema(description = "排序", example = "10")
            Integer sortOrder
    ) {
    }

    @Schema(name = "AcademyArticlePayload", description = "学堂文章摘要")
    public record AcademyArticlePayload(
            @Schema(description = "文章 ID", example = "1001")
            Long id,
            @Schema(description = "所属栏目 ID", example = "1")
            Long categoryId,
            @Schema(description = "所属栏目名称", example = "维修入门")
            String categoryName,
            @Schema(description = "文章标题", example = "空调漏水排查五步法")
            String title,
            @Schema(description = "摘要", example = "先看排水管，再看蒸发器和坡度")
            String summary,
            @Schema(description = "封面图", example = "https://cdn.example.com/academy-1.jpg")
            String coverImage,
            @Schema(description = "作者", example = "平台教研组")
            String author,
            @Schema(description = "浏览量", example = "128")
            Integer viewCount,
            @Schema(description = "是否发布", example = "true")
            boolean published,
            @Schema(description = "发布时间", example = "2026-04-08T11:30:00")
            LocalDateTime publishedAt
    ) {
    }

    @Schema(name = "AcademyArticleDetailPayload", description = "学堂文章详情")
    public record AcademyArticleDetailPayload(
            @Schema(description = "文章 ID", example = "1001")
            Long id,
            @Schema(description = "所属栏目", implementation = AcademyCategoryPayload.class)
            AcademyCategoryPayload category,
            @Schema(description = "标题", example = "空调漏水排查五步法")
            String title,
            @Schema(description = "摘要", example = "先看排水管，再看蒸发器和坡度")
            String summary,
            @Schema(description = "正文内容")
            String content,
            @Schema(description = "封面图", example = "https://cdn.example.com/academy-1.jpg")
            String coverImage,
            @Schema(description = "作者", example = "平台教研组")
            String author,
            @Schema(description = "浏览量", example = "128")
            Integer viewCount,
            @Schema(description = "发布时间", example = "2026-04-08T11:30:00")
            LocalDateTime publishedAt
    ) {
    }

    @Schema(name = "CommunityCommentPayload", description = "圈子评论")
    public record CommunityCommentPayload(
            @Schema(description = "评论 ID", example = "5001")
            Long id,
            @Schema(description = "帖子 ID", example = "3001")
            Long postId,
            @Schema(description = "作者", example = "社区用户")
            String author,
            @Schema(description = "内容", example = "这个经验很实用")
            String content,
            @Schema(description = "发布时间", example = "2026-04-08T11:30:00")
            LocalDateTime createdAt
    ) {
    }

    @Schema(name = "CommunityPostPayload", description = "圈子帖子")
    public record CommunityPostPayload(
            @Schema(description = "帖子 ID", example = "3001")
            Long id,
            @Schema(description = "城市", example = "上海")
            String cityName,
            @Schema(description = "标题", example = "老小区热水器改造经验")
            String title,
            @Schema(description = "摘要", example = "先确认燃气管路，再确认烟道")
            String summary,
            @Schema(description = "正文", example = "先确认燃气管路，再确认烟道与排烟条件")
            String content,
            @Schema(description = "封面图", example = "https://cdn.example.com/community-cover.jpg")
            String coverImage,
            @ArraySchema(schema = @Schema(description = "帖子配图"))
            List<String> images,
            @Schema(description = "作者", example = "沪上师傅老周")
            String author,
            @Schema(description = "点赞数", example = "19")
            Integer likeCount,
            @Schema(description = "评论数", example = "3")
            Integer commentCount,
            @Schema(description = "是否发布", example = "true")
            boolean published,
            @Schema(description = "发布时间", example = "2026-04-08T11:30:00")
            LocalDateTime publishedAt
    ) {
    }

    @Schema(name = "CommunityPostDetailPayload", description = "圈子帖子详情")
    public record CommunityPostDetailPayload(
            @Schema(description = "帖子信息", implementation = CommunityPostPayload.class)
            CommunityPostPayload post,
            @ArraySchema(schema = @Schema(implementation = CommunityCommentPayload.class))
            List<CommunityCommentPayload> comments
    ) {
    }

    @Schema(name = "CreateCommunityPostRequest", description = "发布圈子帖子")
    public record CreateCommunityPostRequest(
            @Schema(description = "城市", example = "上海")
            String cityName,
            @Schema(description = "标题", example = "老小区热水器改造经验")
            String title,
            @Schema(description = "正文", example = "先确认燃气管路，再确认烟道")
            String content,
            @ArraySchema(schema = @Schema(description = "帖子配图"))
            List<String> images
    ) {
    }

    @Schema(name = "CreateCommunityCommentRequest", description = "发布圈子评论")
    public record CreateCommunityCommentRequest(
            @Schema(description = "评论内容", example = "这个经验很实用")
            String content
    ) {
    }

    @Schema(name = "ReportCommunityPostRequest", description = "举报圈子帖子")
    public record ReportCommunityPostRequest(
            @Schema(description = "举报原因", example = "内容不实")
            String reason,
            @Schema(description = "补充说明", example = "图片与描述不符")
            String detail
    ) {
    }

    @Schema(name = "AdminEcosystemCardPayload", description = "后台生态卡配置")
    public record AdminEcosystemCardPayload(
            Long id,
            String name,
            String description,
            String icon,
            String color,
            String link,
            Integer sortOrder,
            Boolean enabled
    ) {
    }

    @Schema(name = "AdminAcademyCategoryPayload", description = "后台学堂栏目配置")
    public record AdminAcademyCategoryPayload(
            Long id,
            String name,
            String icon,
            String description,
            Integer sortOrder,
            Boolean enabled
    ) {
    }

    @Schema(name = "AdminAcademyArticlePayload", description = "后台学堂文章配置")
    public record AdminAcademyArticlePayload(
            Long id,
            Long categoryId,
            String title,
            String summary,
            String content,
            String coverImage,
            String author,
            Integer viewCount,
            Integer sortOrder,
            Boolean published
    ) {
    }

    @Schema(name = "AdminCommunityPostPayload", description = "后台圈子帖子配置")
    public record AdminCommunityPostPayload(
            Long id,
            String cityName,
            String title,
            String content,
            String coverImage,
            String images,
            String authorName,
            Integer likeCount,
            Integer commentCount,
            Boolean published
    ) {
    }

    @Schema(name = "AdminCommunityReportPayload", description = "后台圈子举报")
    public record AdminCommunityReportPayload(
            Long id,
            Long postId,
            String reason,
            String detail,
            String reporterName,
            String status,
            Boolean handled
    ) {
    }
}
