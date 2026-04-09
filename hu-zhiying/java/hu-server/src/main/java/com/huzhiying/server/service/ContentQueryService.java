package com.huzhiying.server.service;

import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceItem;
import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyArticleEntity;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyCategoryEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityCommentEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityPostEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityReportEntity;
import com.huzhiying.server.persistence.PersistenceEntities.EcosystemCardEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ContentQueryService {

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final MapService mapService;

    public ContentQueryService(PlatformRepository platformRepository,
                               PlatformAssembler assembler,
                               MapService mapService) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.mapService = mapService;
    }

    public ContentDtos.HomePayload homeData() {
        List<ServiceCategory> categoryTree = categories();
        List<SearchDocument> documents = searchDocuments();
        List<ContentDtos.HomeCategoryNavPayload> categoryNav = categoryTree.stream()
                .map(category -> new ContentDtos.HomeCategoryNavPayload(
                        category.id(),
                        category.name(),
                        category.icon(),
                        category.services().stream().limit(4).map(ServiceItem::name).toList()
                ))
                .toList();

        List<String> hotKeywords = documents.stream()
                .limit(6)
                .map(SearchDocument::title)
                .toList();

        List<ContentDtos.RecommendationPayload> recommendations = documents.stream()
                .limit(8)
                .map(document -> new ContentDtos.RecommendationPayload(
                        extractDocumentNumericId(document.id()),
                        document.type(),
                        document.title(),
                        "product".equals(document.type()) ? "商城推荐" : "上门服务",
                        document.price(),
                        "1.2k",
                        document.icon()
                ))
                .toList();

        return new ContentDtos.HomePayload(
                "上海",
                mapService.serviceCities(),
                hotKeywords,
                platformRepository.listBanners().stream().map(assembler::toBanner).toList(),
                platformRepository.listNotices().stream().map(assembler::toNotice).toList(),
                categoryNav,
                new ContentDtos.SvipCardPayload(
                        "SVIP 金管家",
                        "专属客服、优先派单、年度保养与会员礼包",
                        List.of("专属客服", "优先派单", "年度保养", "上门优惠")
                ),
                platformRepository.listEcosystemCards().stream()
                        .filter(card -> Boolean.TRUE.equals(card.enabled))
                        .map(this::toEcosystemCard)
                        .toList(),
                recommendations
        );
    }

    public List<ContentDtos.AcademyCategoryPayload> academyCategories() {
        return platformRepository.listAcademyCategories(true).stream()
                .map(this::toAcademyCategory)
                .toList();
    }

    public List<ContentDtos.AcademyArticlePayload> academyArticles(Long categoryId) {
        Map<Long, AcademyCategoryEntity> categoryMap = platformRepository.listAcademyCategories(true).stream()
                .collect(LinkedHashMap::new, (map, item) -> map.put(item.id, item), Map::putAll);
        return platformRepository.listAcademyArticles(categoryId, true).stream()
                .map(article -> toAcademyArticle(article, categoryMap.get(article.categoryId)))
                .toList();
    }

    public ContentDtos.AcademyArticleDetailPayload academyArticle(Long id) {
        AcademyArticleEntity article = platformRepository.findAcademyArticle(id)
                .filter(item -> Boolean.TRUE.equals(item.enabled) && Boolean.TRUE.equals(item.published))
                .orElseThrow();
        AcademyCategoryEntity category = platformRepository.findAcademyCategory(article.categoryId).orElseThrow();
        return new ContentDtos.AcademyArticleDetailPayload(
                article.id,
                toAcademyCategory(category),
                article.title,
                article.summaryText,
                article.contentText,
                article.coverImage,
                article.authorName,
                0,
                article.createdAt
        );
    }

    public List<ContentDtos.CommunityPostPayload> communityPosts(String cityName) {
        return platformRepository.listCommunityPosts(cityName, true).stream()
                .map(this::toCommunityPost)
                .toList();
    }

    public ContentDtos.CommunityPostDetailPayload communityPost(Long id) {
        CommunityPostEntity post = platformRepository.findCommunityPost(id)
                .filter(item -> "PUBLISHED".equalsIgnoreCase(item.statusCode))
                .orElseThrow();
        return new ContentDtos.CommunityPostDetailPayload(
                toCommunityPost(post),
                platformRepository.listCommunityComments(post.id).stream()
                        .map(this::toCommunityComment)
                        .toList()
        );
    }

    public List<ContentDtos.CommunityCommentPayload> communityComments(Long postId) {
        return platformRepository.listCommunityComments(postId).stream()
                .map(this::toCommunityComment)
                .toList();
    }

    private List<ServiceCategory> categories() {
        List<ServiceCategory> categories = new ArrayList<>();
        Map<Long, List<ServiceItem>> groupedItems = assembler.groupServiceItems(platformRepository.listServiceItems());
        platformRepository.listServiceCategories().forEach(category ->
                categories.add(assembler.toServiceCategory(category, groupedItems.getOrDefault(category.id, List.of())))
        );
        return categories;
    }

    private List<SearchDocument> searchDocuments() {
        List<SearchDocument> documents = new ArrayList<>();
        platformRepository.listServiceItems().forEach(item -> documents.add(
                assembler.toSearchDocument("service-" + item.id, "service", item.name, item.subtitle, item.basePrice, "/static/icons/screwdriver.svg")
        ));
        platformRepository.listProducts().forEach(item -> documents.add(
                assembler.toSearchDocument("product-" + item.id, "product", item.name, item.descriptionText, item.price, "/static/icons/mall.svg")
        ));
        return documents;
    }

    private ContentDtos.EcosystemCardPayload toEcosystemCard(EcosystemCardEntity entity) {
        return new ContentDtos.EcosystemCardPayload(
                entity.id,
                entity.name,
                entity.descriptionText,
                entity.icon,
                entity.color,
                entity.link
        );
    }

    private ContentDtos.AcademyCategoryPayload toAcademyCategory(AcademyCategoryEntity entity) {
        return new ContentDtos.AcademyCategoryPayload(
                entity.id,
                entity.name,
                entity.icon,
                entity.descriptionText,
                Boolean.TRUE.equals(entity.enabled),
                entity.sortOrder
        );
    }

    private ContentDtos.AcademyArticlePayload toAcademyArticle(AcademyArticleEntity entity, AcademyCategoryEntity category) {
        return new ContentDtos.AcademyArticlePayload(
                entity.id,
                entity.categoryId,
                category == null ? "" : category.name,
                entity.title,
                entity.summaryText,
                entity.coverImage,
                entity.authorName,
                0,
                Boolean.TRUE.equals(entity.published),
                entity.createdAt
        );
    }

    private ContentDtos.CommunityPostPayload toCommunityPost(CommunityPostEntity entity) {
        UserEntity author = platformRepository.findUser(entity.userId).orElse(null);
        List<String> images = assembler.splitList(entity.imagesText);
        return new ContentDtos.CommunityPostPayload(
                entity.id,
                entity.cityName,
                entity.title,
                summarize(entity.contentText),
                entity.contentText,
                images.isEmpty() ? "/static/icons/community.svg" : images.get(0),
                images,
                author == null ? "平台用户" : author.nickname,
                defaultInteger(entity.likeCount),
                defaultInteger(entity.commentCount),
                "PUBLISHED".equalsIgnoreCase(entity.statusCode),
                entity.createdAt
        );
    }

    private ContentDtos.CommunityCommentPayload toCommunityComment(CommunityCommentEntity entity) {
        UserEntity author = platformRepository.findUser(entity.userId).orElse(null);
        return new ContentDtos.CommunityCommentPayload(
                entity.id,
                entity.postId,
                author == null ? "平台用户" : author.nickname,
                entity.contentText,
                entity.createdAt
        );
    }

    private long extractDocumentNumericId(String id) {
        String digits = id == null ? "" : id.replaceAll("\\D", "");
        if (digits.isBlank()) {
            return 0L;
        }
        return Long.parseLong(digits);
    }

    private int defaultInteger(Integer value) {
        return value == null ? 0 : value;
    }

    private String summarize(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        return content.length() > 40 ? content.substring(0, 40) + "..." : content;
    }
}
