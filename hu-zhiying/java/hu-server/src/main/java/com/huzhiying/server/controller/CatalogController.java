package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "mobile-catalog", description = "移动端类目、商品、首页与搜索接口")
public class CatalogController {

    private final PlatformFacadeService platformFacadeService;

    public CatalogController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/home")
    @Operation(summary = "查询首页聚合数据")
    public ApiResponse<?> home() {
        return ApiResponse.success(platformFacadeService.homeData());
    }

    @GetMapping("/api/categories")
    @Operation(summary = "查询服务类目")
    public ApiResponse<?> categories() {
        return ApiResponse.success(platformFacadeService.categories());
    }

    @GetMapping("/api/services")
    @Operation(summary = "查询服务列表")
    public ApiResponse<?> services() {
        return ApiResponse.success(platformFacadeService.services());
    }

    @GetMapping("/api/services/{id}")
    @Operation(summary = "查询服务详情")
    public ApiResponse<?> serviceDetail(@PathVariable("id") Long id) {
        return ApiResponse.success(platformFacadeService.serviceDetail(id));
    }

    @GetMapping("/api/services/{id}/comments")
    @Operation(summary = "查询服务评价列表")
    public ApiResponse<?> serviceComments(@PathVariable("id") Long id) {
        return ApiResponse.success(platformFacadeService.serviceComments(id));
    }

    @GetMapping("/api/products")
    @Operation(summary = "查询商品列表")
    public ApiResponse<?> products() {
        return ApiResponse.success(platformFacadeService.products());
    }

    @GetMapping("/api/products/{id}")
    @Operation(summary = "查询商品详情")
    public ApiResponse<?> productDetail(@PathVariable("id") Long id) {
        return ApiResponse.success(platformFacadeService.productDetail(id));
    }

    @GetMapping("/api/search")
    @Operation(summary = "搜索服务与商品")
    public ApiResponse<?> search(@RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResponse.success(platformFacadeService.search(keyword));
    }
}
