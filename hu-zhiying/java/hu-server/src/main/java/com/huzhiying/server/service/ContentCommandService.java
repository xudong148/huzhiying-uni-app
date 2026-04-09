package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityCommentEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityPostEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityReportEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ContentCommandService {

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final AuthSessionService authSessionService;

    public ContentCommandService(PlatformRepository platformRepository,
                                 PlatformAssembler assembler,
                                 AuthSessionService authSessionService) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.authSessionService = authSessionService;
    }

    public ContentDtos.CommunityPostPayload createCommunityPost(ContentDtos.CreateCommunityPostRequest request) {
        String title = trimRequired(request.title(), "帖子标题");
        String content = trimRequired(request.content(), "帖子正文");
        List<String> images = request.images() == null ? List.of() : request.images().stream()
                .filter(item -> item != null && !item.isBlank())
                .toList();
        UserEntity user = authSessionService.currentUser(RoleCode.USER);

        CommunityPostEntity entity = new CommunityPostEntity();
        entity.id = platformRepository.nextLongId("CommunityPostEntity", "id", 3000L);
        entity.userId = user.id;
        entity.cityName = trimToDefault(request.cityName(), "上海");
        entity.title = title;
        entity.contentText = content;
        entity.imagesText = String.join("|", images);
        entity.likeCount = 0;
        entity.commentCount = 0;
        entity.statusCode = "PUBLISHED";
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        return toCommunityPost(platformRepository.saveCommunityPost(entity), user);
    }

    public ContentDtos.CommunityCommentPayload createCommunityComment(Long postId, ContentDtos.CreateCommunityCommentRequest request) {
        CommunityPostEntity post = platformRepository.findCommunityPost(postId)
                .filter(item -> "PUBLISHED".equalsIgnoreCase(item.statusCode))
                .orElseThrow();
        UserEntity user = authSessionService.currentUser(RoleCode.USER);

        CommunityCommentEntity entity = new CommunityCommentEntity();
        entity.id = platformRepository.nextLongId("CommunityCommentEntity", "id", 5000L);
        entity.postId = post.id;
        entity.userId = user.id;
        entity.contentText = trimRequired(request.content(), "评论内容");
        entity.createdAt = LocalDateTime.now();
        CommunityCommentEntity saved = platformRepository.saveCommunityComment(entity);

        post.commentCount = defaultInteger(post.commentCount) + 1;
        post.updatedAt = LocalDateTime.now();
        platformRepository.saveCommunityPost(post);
        return new ContentDtos.CommunityCommentPayload(saved.id, saved.postId, user.nickname, saved.contentText, saved.createdAt);
    }

    public ContentDtos.CommunityPostPayload likeCommunityPost(Long postId) {
        CommunityPostEntity post = platformRepository.findCommunityPost(postId)
                .filter(item -> "PUBLISHED".equalsIgnoreCase(item.statusCode))
                .orElseThrow();
        post.likeCount = defaultInteger(post.likeCount) + 1;
        post.updatedAt = LocalDateTime.now();
        CommunityPostEntity saved = platformRepository.saveCommunityPost(post);
        UserEntity user = platformRepository.findUser(saved.userId).orElse(null);
        return toCommunityPost(saved, user);
    }

    public ContentDtos.AdminCommunityReportPayload reportCommunityPost(Long postId, ContentDtos.ReportCommunityPostRequest request) {
        CommunityPostEntity post = platformRepository.findCommunityPost(postId)
                .filter(item -> "PUBLISHED".equalsIgnoreCase(item.statusCode))
                .orElseThrow();
        UserEntity user = authSessionService.currentUser(RoleCode.USER);

        CommunityReportEntity entity = new CommunityReportEntity();
        entity.id = platformRepository.nextLongId("CommunityReportEntity", "id", 7000L);
        entity.postId = post.id;
        entity.reporterUserId = user.id;
        entity.reasonText = trimRequired(request.reason(), "举报原因");
        entity.statusCode = "PENDING";
        entity.createdAt = LocalDateTime.now();
        CommunityReportEntity saved = platformRepository.saveCommunityReport(entity);
        return new ContentDtos.AdminCommunityReportPayload(
                saved.id,
                saved.postId,
                saved.reasonText,
                request.detail(),
                user.nickname,
                saved.statusCode,
                saved.handledAt != null
        );
    }

    private ContentDtos.CommunityPostPayload toCommunityPost(CommunityPostEntity entity, UserEntity user) {
        List<String> images = assembler.splitList(entity.imagesText);
        return new ContentDtos.CommunityPostPayload(
                entity.id,
                entity.cityName,
                entity.title,
                summarize(entity.contentText),
                entity.contentText,
                images.isEmpty() ? "/static/icons/community.svg" : images.get(0),
                images,
                user == null ? "平台用户" : user.nickname,
                defaultInteger(entity.likeCount),
                defaultInteger(entity.commentCount),
                "PUBLISHED".equalsIgnoreCase(entity.statusCode),
                entity.createdAt
        );
    }

    private String trimRequired(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + "不能为空");
        }
        return value.trim();
    }

    private String trimToDefault(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
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
