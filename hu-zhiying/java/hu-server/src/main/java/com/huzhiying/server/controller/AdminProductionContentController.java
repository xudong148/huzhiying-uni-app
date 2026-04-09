package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.service.AdminContentOpsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "admin-production-content", description = "生产化内容运营配置")
public class AdminProductionContentController extends AdminConfigControllerSupport {

    private final AdminContentOpsService adminContentOpsService;

    public AdminProductionContentController(AdminContentOpsService adminContentOpsService) {
        this.adminContentOpsService = adminContentOpsService;
    }

    @GetMapping("/api/admin/content/ecosystem-cards")
    @Operation(summary = "查询生态卡配置")
    public ApiResponse<?> ecosystemCards() {
        return ApiResponse.success(adminContentOpsService.ecosystemCards());
    }

    @GetMapping("/api/admin/content/ecosystem-cards/{id}")
    @Operation(summary = "查询生态卡详情")
    public ApiResponse<?> ecosystemCard(@PathVariable("id") Long id) {
        return ApiResponse.success(adminContentOpsService.ecosystemCard(id));
    }

    @PostMapping("/api/admin/content/ecosystem-cards")
    @Operation(summary = "新增生态卡")
    public ApiResponse<?> createEcosystemCard(@RequestBody ContentDtos.AdminEcosystemCardPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveEcosystemCard(null, payload));
    }

    @PutMapping("/api/admin/content/ecosystem-cards/{id}")
    @Operation(summary = "更新生态卡")
    public ApiResponse<?> updateEcosystemCard(@PathVariable("id") Long id,
                                              @RequestBody ContentDtos.AdminEcosystemCardPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveEcosystemCard(id, payload));
    }

    @DeleteMapping("/api/admin/content/ecosystem-cards/{id}")
    @Operation(summary = "删除生态卡")
    public ApiResponse<Boolean> deleteEcosystemCard(@PathVariable("id") Long id) {
        adminContentOpsService.deleteEcosystemCard(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/academy-categories")
    @Operation(summary = "查询学堂栏目")
    public ApiResponse<?> academyCategories() {
        return ApiResponse.success(adminContentOpsService.academyCategories());
    }

    @GetMapping("/api/admin/content/academy-categories/{id}")
    @Operation(summary = "查询学堂栏目详情")
    public ApiResponse<?> academyCategory(@PathVariable("id") Long id) {
        return ApiResponse.success(adminContentOpsService.academyCategory(id));
    }

    @PostMapping("/api/admin/content/academy-categories")
    @Operation(summary = "新增学堂栏目")
    public ApiResponse<?> createAcademyCategory(@RequestBody ContentDtos.AdminAcademyCategoryPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveAcademyCategory(null, payload));
    }

    @PutMapping("/api/admin/content/academy-categories/{id}")
    @Operation(summary = "更新学堂栏目")
    public ApiResponse<?> updateAcademyCategory(@PathVariable("id") Long id,
                                                @RequestBody ContentDtos.AdminAcademyCategoryPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveAcademyCategory(id, payload));
    }

    @DeleteMapping("/api/admin/content/academy-categories/{id}")
    @Operation(summary = "删除学堂栏目")
    public ApiResponse<Boolean> deleteAcademyCategory(@PathVariable("id") Long id) {
        adminContentOpsService.deleteAcademyCategory(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/academy-articles")
    @Operation(summary = "查询学堂文章")
    public ApiResponse<?> academyArticles() {
        return ApiResponse.success(adminContentOpsService.academyArticles());
    }

    @GetMapping("/api/admin/content/academy-articles/{id}")
    @Operation(summary = "查询学堂文章详情")
    public ApiResponse<?> academyArticle(@PathVariable("id") Long id) {
        return ApiResponse.success(adminContentOpsService.academyArticle(id));
    }

    @PostMapping("/api/admin/content/academy-articles")
    @Operation(summary = "新增学堂文章")
    public ApiResponse<?> createAcademyArticle(@RequestBody ContentDtos.AdminAcademyArticlePayload payload) {
        return ApiResponse.success(adminContentOpsService.saveAcademyArticle(null, payload));
    }

    @PutMapping("/api/admin/content/academy-articles/{id}")
    @Operation(summary = "更新学堂文章")
    public ApiResponse<?> updateAcademyArticle(@PathVariable("id") Long id,
                                               @RequestBody ContentDtos.AdminAcademyArticlePayload payload) {
        return ApiResponse.success(adminContentOpsService.saveAcademyArticle(id, payload));
    }

    @DeleteMapping("/api/admin/content/academy-articles/{id}")
    @Operation(summary = "删除学堂文章")
    public ApiResponse<Boolean> deleteAcademyArticle(@PathVariable("id") Long id) {
        adminContentOpsService.deleteAcademyArticle(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/community-posts")
    @Operation(summary = "查询圈子帖子")
    public ApiResponse<?> communityPosts() {
        return ApiResponse.success(adminContentOpsService.communityPosts());
    }

    @GetMapping("/api/admin/content/community-posts/{id}")
    @Operation(summary = "查询圈子帖子详情")
    public ApiResponse<?> communityPost(@PathVariable("id") Long id) {
        return ApiResponse.success(adminContentOpsService.communityPost(id));
    }

    @PostMapping("/api/admin/content/community-posts")
    @Operation(summary = "新增圈子帖子")
    public ApiResponse<?> createCommunityPost(@RequestBody ContentDtos.AdminCommunityPostPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveCommunityPost(null, payload));
    }

    @PutMapping("/api/admin/content/community-posts/{id}")
    @Operation(summary = "更新圈子帖子")
    public ApiResponse<?> updateCommunityPost(@PathVariable("id") Long id,
                                              @RequestBody ContentDtos.AdminCommunityPostPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveCommunityPost(id, payload));
    }

    @DeleteMapping("/api/admin/content/community-posts/{id}")
    @Operation(summary = "删除圈子帖子")
    public ApiResponse<Boolean> deleteCommunityPost(@PathVariable("id") Long id) {
        adminContentOpsService.deleteCommunityPost(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/content/community-reports")
    @Operation(summary = "查询圈子举报")
    public ApiResponse<?> communityReports() {
        return ApiResponse.success(adminContentOpsService.communityReports());
    }

    @GetMapping("/api/admin/content/community-reports/{id}")
    @Operation(summary = "查询圈子举报详情")
    public ApiResponse<?> communityReport(@PathVariable("id") Long id) {
        return ApiResponse.success(adminContentOpsService.communityReport(id));
    }

    @PostMapping("/api/admin/content/community-reports")
    @Operation(summary = "新增圈子举报")
    public ApiResponse<?> createCommunityReport(@RequestBody ContentDtos.AdminCommunityReportPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveCommunityReport(null, payload));
    }

    @PutMapping("/api/admin/content/community-reports/{id}")
    @Operation(summary = "更新圈子举报")
    public ApiResponse<?> updateCommunityReport(@PathVariable("id") Long id,
                                                @RequestBody ContentDtos.AdminCommunityReportPayload payload) {
        return ApiResponse.success(adminContentOpsService.saveCommunityReport(id, payload));
    }

    @DeleteMapping("/api/admin/content/community-reports/{id}")
    @Operation(summary = "删除圈子举报")
    public ApiResponse<Boolean> deleteCommunityReport(@PathVariable("id") Long id) {
        adminContentOpsService.deleteCommunityReport(id);
        return ApiResponse.success(true);
    }
}
