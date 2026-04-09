package com.huzhiying.server.service;

import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyArticleEntity;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyCategoryEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityPostEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityReportEntity;
import com.huzhiying.server.persistence.PersistenceEntities.EcosystemCardEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminContentOpsService {

    private final PlatformRepository platformRepository;

    public AdminContentOpsService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public List<ContentDtos.AdminEcosystemCardPayload> ecosystemCards() {
        return platformRepository.listEcosystemCards().stream()
                .map(this::toEcosystemCard)
                .toList();
    }

    public ContentDtos.AdminEcosystemCardPayload ecosystemCard(Long id) {
        return toEcosystemCard(platformRepository.findById(EcosystemCardEntity.class, id).orElseThrow());
    }

    public ContentDtos.AdminEcosystemCardPayload saveEcosystemCard(Long id, ContentDtos.AdminEcosystemCardPayload payload) {
        EcosystemCardEntity entity = id == null ? new EcosystemCardEntity()
                : platformRepository.findById(EcosystemCardEntity.class, id).orElseThrow();
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("EcosystemCardEntity", "id", 0L);
        }
        entity.name = trimRequired(payload.name(), "生态卡名称");
        entity.descriptionText = trimToNull(payload.description());
        entity.icon = trimRequired(payload.icon(), "生态卡图标");
        entity.color = trimToDefault(payload.color(), "#2B5CFF");
        entity.link = trimRequired(payload.link(), "生态卡跳转");
        entity.sortOrder = intValue(payload.sortOrder(), 0);
        entity.enabled = boolValue(payload.enabled(), true);
        return toEcosystemCard(platformRepository.saveEntity(entity));
    }

    public void deleteEcosystemCard(Long id) {
        platformRepository.deleteEntity(EcosystemCardEntity.class, id);
    }

    public List<ContentDtos.AdminAcademyCategoryPayload> academyCategories() {
        return platformRepository.listAcademyCategories(false).stream()
                .map(this::toAcademyCategory)
                .toList();
    }

    public ContentDtos.AdminAcademyCategoryPayload academyCategory(Long id) {
        return toAcademyCategory(platformRepository.findById(AcademyCategoryEntity.class, id).orElseThrow());
    }

    public ContentDtos.AdminAcademyCategoryPayload saveAcademyCategory(Long id, ContentDtos.AdminAcademyCategoryPayload payload) {
        AcademyCategoryEntity entity = id == null ? new AcademyCategoryEntity()
                : platformRepository.findById(AcademyCategoryEntity.class, id).orElseThrow();
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("AcademyCategoryEntity", "id", 0L);
        }
        entity.name = trimRequired(payload.name(), "学堂栏目名称");
        entity.icon = trimToDefault(payload.icon(), "/static/icons/school.svg");
        entity.descriptionText = trimToNull(payload.description());
        entity.sortOrder = intValue(payload.sortOrder(), 0);
        entity.enabled = boolValue(payload.enabled(), true);
        return toAcademyCategory(platformRepository.saveAcademyCategory(entity));
    }

    public void deleteAcademyCategory(Long id) {
        platformRepository.deleteEntity(AcademyCategoryEntity.class, id);
    }

    public List<ContentDtos.AdminAcademyArticlePayload> academyArticles() {
        return platformRepository.listAcademyArticles(null, false).stream()
                .map(this::toAcademyArticle)
                .toList();
    }

    public ContentDtos.AdminAcademyArticlePayload academyArticle(Long id) {
        return toAcademyArticle(platformRepository.findById(AcademyArticleEntity.class, id).orElseThrow());
    }

    public ContentDtos.AdminAcademyArticlePayload saveAcademyArticle(Long id, ContentDtos.AdminAcademyArticlePayload payload) {
        AcademyArticleEntity entity = id == null ? new AcademyArticleEntity()
                : platformRepository.findById(AcademyArticleEntity.class, id).orElseThrow();
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("AcademyArticleEntity", "id", 1000L);
            entity.createdAt = LocalDateTime.now();
        }
        platformRepository.findById(AcademyCategoryEntity.class, payload.categoryId()).orElseThrow();
        entity.categoryId = payload.categoryId();
        entity.title = trimRequired(payload.title(), "文章标题");
        entity.summaryText = trimToNull(payload.summary());
        entity.contentText = trimRequired(payload.content(), "文章正文");
        entity.coverImage = trimToDefault(payload.coverImage(), "/static/icons/school.svg");
        entity.authorName = trimToDefault(payload.author(), "平台教研组");
        entity.sortOrder = intValue(payload.sortOrder(), 0);
        entity.published = boolValue(payload.published(), true);
        entity.enabled = true;
        entity.updatedAt = LocalDateTime.now();
        return toAcademyArticle(platformRepository.saveAcademyArticle(entity));
    }

    public void deleteAcademyArticle(Long id) {
        platformRepository.deleteEntity(AcademyArticleEntity.class, id);
    }

    public List<ContentDtos.AdminCommunityPostPayload> communityPosts() {
        return platformRepository.listCommunityPosts(null, false).stream()
                .map(this::toCommunityPost)
                .toList();
    }

    public ContentDtos.AdminCommunityPostPayload communityPost(Long id) {
        return toCommunityPost(platformRepository.findById(CommunityPostEntity.class, id).orElseThrow());
    }

    public ContentDtos.AdminCommunityPostPayload saveCommunityPost(Long id, ContentDtos.AdminCommunityPostPayload payload) {
        CommunityPostEntity entity = id == null ? new CommunityPostEntity()
                : platformRepository.findById(CommunityPostEntity.class, id).orElseThrow();
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("CommunityPostEntity", "id", 3000L);
            entity.createdAt = LocalDateTime.now();
            entity.userId = 1L;
        }
        entity.cityName = trimToDefault(payload.cityName(), "上海");
        entity.title = trimRequired(payload.title(), "帖子标题");
        entity.contentText = trimRequired(payload.content(), "帖子正文");
        entity.imagesText = mergeImages(payload.coverImage(), payload.images());
        entity.likeCount = intValue(payload.likeCount(), 0);
        entity.commentCount = intValue(payload.commentCount(), 0);
        entity.statusCode = boolValue(payload.published(), true) ? "PUBLISHED" : "DRAFT";
        entity.updatedAt = LocalDateTime.now();
        return toCommunityPost(platformRepository.saveCommunityPost(entity));
    }

    public void deleteCommunityPost(Long id) {
        platformRepository.deleteEntity(CommunityPostEntity.class, id);
    }

    public List<ContentDtos.AdminCommunityReportPayload> communityReports() {
        return platformRepository.listCommunityReports().stream()
                .map(this::toCommunityReport)
                .toList();
    }

    public ContentDtos.AdminCommunityReportPayload communityReport(Long id) {
        return toCommunityReport(platformRepository.findById(CommunityReportEntity.class, id).orElseThrow());
    }

    public ContentDtos.AdminCommunityReportPayload saveCommunityReport(Long id, ContentDtos.AdminCommunityReportPayload payload) {
        CommunityReportEntity entity = id == null ? new CommunityReportEntity()
                : platformRepository.findById(CommunityReportEntity.class, id).orElseThrow();
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("CommunityReportEntity", "id", 7000L);
            entity.createdAt = LocalDateTime.now();
            entity.reporterUserId = 1L;
        }
        platformRepository.findById(CommunityPostEntity.class, payload.postId()).orElseThrow();
        entity.postId = payload.postId();
        entity.reasonText = trimRequired(payload.reason(), "举报原因");
        entity.statusCode = trimToDefault(payload.status(), boolValue(payload.handled(), false) ? "HANDLED" : "PENDING");
        entity.handledAt = boolValue(payload.handled(), false) ? LocalDateTime.now() : null;
        return toCommunityReport(platformRepository.saveCommunityReport(entity));
    }

    public void deleteCommunityReport(Long id) {
        platformRepository.deleteEntity(CommunityReportEntity.class, id);
    }

    private ContentDtos.AdminEcosystemCardPayload toEcosystemCard(EcosystemCardEntity entity) {
        return new ContentDtos.AdminEcosystemCardPayload(
                entity.id,
                entity.name,
                entity.descriptionText,
                entity.icon,
                entity.color,
                entity.link,
                entity.sortOrder,
                entity.enabled
        );
    }

    private ContentDtos.AdminAcademyCategoryPayload toAcademyCategory(AcademyCategoryEntity entity) {
        return new ContentDtos.AdminAcademyCategoryPayload(
                entity.id,
                entity.name,
                entity.icon,
                entity.descriptionText,
                entity.sortOrder,
                entity.enabled
        );
    }

    private ContentDtos.AdminAcademyArticlePayload toAcademyArticle(AcademyArticleEntity entity) {
        return new ContentDtos.AdminAcademyArticlePayload(
                entity.id,
                entity.categoryId,
                entity.title,
                entity.summaryText,
                entity.contentText,
                entity.coverImage,
                entity.authorName,
                0,
                entity.sortOrder,
                entity.published
        );
    }

    private ContentDtos.AdminCommunityPostPayload toCommunityPost(CommunityPostEntity entity) {
        UserEntity author = platformRepository.findUser(entity.userId).orElse(null);
        List<String> images = entity.imagesText == null ? List.of() : List.of(entity.imagesText.split("\\|"));
        String coverImage = images.isEmpty() ? "" : images.get(0);
        return new ContentDtos.AdminCommunityPostPayload(
                entity.id,
                entity.cityName,
                entity.title,
                entity.contentText,
                coverImage,
                entity.imagesText,
                author == null ? "平台用户" : author.nickname,
                intValue(entity.likeCount, 0),
                intValue(entity.commentCount, 0),
                "PUBLISHED".equalsIgnoreCase(entity.statusCode)
        );
    }

    private ContentDtos.AdminCommunityReportPayload toCommunityReport(CommunityReportEntity entity) {
        UserEntity reporter = platformRepository.findUser(entity.reporterUserId).orElse(null);
        return new ContentDtos.AdminCommunityReportPayload(
                entity.id,
                entity.postId,
                entity.reasonText,
                "",
                reporter == null ? "平台用户" : reporter.nickname,
                entity.statusCode,
                entity.handledAt != null
        );
    }

    private String mergeImages(String coverImage, String imagesText) {
        String extra = trimToNull(imagesText);
        String cover = trimToNull(coverImage);
        if (cover == null) {
            return extra;
        }
        if (extra == null) {
            return cover;
        }
        return cover + "|" + extra;
    }

    private String trimRequired(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + "不能为空");
        }
        return value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String trimToDefault(String value, String defaultValue) {
        String trimmed = trimToNull(value);
        return trimmed == null ? defaultValue : trimmed;
    }

    private Integer intValue(Integer value, int defaultValue) {
        return value == null ? defaultValue : Math.max(value, 0);
    }

    private Boolean boolValue(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;
    }
}
