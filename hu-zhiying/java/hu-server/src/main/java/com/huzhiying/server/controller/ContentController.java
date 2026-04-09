package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.service.ContentCommandService;
import com.huzhiying.server.service.ContentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "mobile-content", description = "学堂与圈子内容接口")
public class ContentController extends ApiControllerSupport {

    private final ContentQueryService contentQueryService;
    private final ContentCommandService contentCommandService;

    public ContentController(ContentQueryService contentQueryService,
                             ContentCommandService contentCommandService) {
        this.contentQueryService = contentQueryService;
        this.contentCommandService = contentCommandService;
    }

    @GetMapping("/api/academy/categories")
    @Operation(summary = "查询学堂栏目")
    public ApiResponse<?> academyCategories() {
        return ApiResponse.success(contentQueryService.academyCategories());
    }

    @GetMapping("/api/academy/articles")
    @Operation(summary = "查询学堂文章")
    public ApiResponse<?> academyArticles(@RequestParam(value = "categoryId", required = false) Long categoryId) {
        return ApiResponse.success(contentQueryService.academyArticles(categoryId));
    }

    @GetMapping("/api/academy/articles/{id}")
    @Operation(summary = "查询学堂文章详情")
    public ApiResponse<?> academyArticle(@PathVariable("id") Long id) {
        return ApiResponse.success(contentQueryService.academyArticle(id));
    }

    @GetMapping("/api/community/posts")
    @Operation(summary = "查询圈子帖子")
    public ApiResponse<?> communityPosts(@RequestParam(value = "cityName", required = false) String cityName) {
        return ApiResponse.success(contentQueryService.communityPosts(cityName));
    }

    @GetMapping("/api/community/posts/{id}")
    @Operation(summary = "查询圈子帖子详情")
    public ApiResponse<?> communityPost(@PathVariable("id") Long id) {
        return ApiResponse.success(contentQueryService.communityPost(id));
    }

    @GetMapping("/api/community/posts/{id}/comments")
    @Operation(summary = "查询圈子评论")
    public ApiResponse<?> communityComments(@PathVariable("id") Long id) {
        return ApiResponse.success(contentQueryService.communityComments(id));
    }

    @PostMapping("/api/community/posts")
    @Operation(summary = "发布圈子帖子")
    public ApiResponse<?> createCommunityPost(@RequestBody ContentDtos.CreateCommunityPostRequest request) {
        return ApiResponse.success(contentCommandService.createCommunityPost(request));
    }

    @PostMapping("/api/community/posts/{id}/comments")
    @Operation(summary = "发布圈子评论")
    public ApiResponse<?> createCommunityComment(@PathVariable("id") Long id,
                                                 @RequestBody ContentDtos.CreateCommunityCommentRequest request) {
        return ApiResponse.success(contentCommandService.createCommunityComment(id, request));
    }

    @PostMapping("/api/community/posts/{id}/like")
    @Operation(summary = "点赞圈子帖子")
    public ApiResponse<?> likeCommunityPost(@PathVariable("id") Long id) {
        return ApiResponse.success(contentCommandService.likeCommunityPost(id));
    }

    @PostMapping("/api/community/posts/{id}/report")
    @Operation(summary = "举报圈子帖子")
    public ApiResponse<?> reportCommunityPost(@PathVariable("id") Long id,
                                              @RequestBody ContentDtos.ReportCommunityPostRequest request) {
        return ApiResponse.success(contentCommandService.reportCommunityPost(id, request));
    }
}
