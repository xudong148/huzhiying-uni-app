package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.AdminConfigDtos;
import com.huzhiying.server.service.AdminConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台类目与商品配置接口。
 */
@RestController
@Tag(name = "admin-catalog", description = "后台类目、服务项、商品和 SKU 配置")
public class AdminCatalogController {

    private final AdminConfigService adminConfigService;

    public AdminCatalogController(AdminConfigService adminConfigService) {
        this.adminConfigService = adminConfigService;
    }

    @GetMapping("/api/admin/catalog/categories")
    @Operation(summary = "查询服务类目列表")
    public ApiResponse<List<AdminConfigDtos.ServiceCategoryPayload>> categories() {
        return ApiResponse.success(adminConfigService.listCategories());
    }

    @GetMapping("/api/admin/catalog/categories/{id}")
    @Operation(summary = "查询服务类目详情")
    public ApiResponse<AdminConfigDtos.ServiceCategoryPayload> category(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getCategory(id));
    }

    @PostMapping("/api/admin/catalog/categories")
    @Operation(summary = "新增服务类目")
    public ApiResponse<AdminConfigDtos.ServiceCategoryPayload> createCategory(@RequestBody AdminConfigDtos.ServiceCategoryPayload payload) {
        return ApiResponse.success(adminConfigService.saveCategory(null, payload));
    }

    @PutMapping("/api/admin/catalog/categories/{id}")
    @Operation(summary = "更新服务类目")
    public ApiResponse<AdminConfigDtos.ServiceCategoryPayload> updateCategory(@PathVariable Long id,
                                                                              @RequestBody AdminConfigDtos.ServiceCategoryPayload payload) {
        return ApiResponse.success(adminConfigService.saveCategory(id, payload));
    }

    @DeleteMapping("/api/admin/catalog/categories/{id}")
    @Operation(summary = "删除服务类目")
    public ApiResponse<Boolean> deleteCategory(@PathVariable Long id) {
        adminConfigService.deleteCategory(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/catalog/service-items")
    @Operation(summary = "查询服务项列表")
    public ApiResponse<List<AdminConfigDtos.ServiceItemPayload>> serviceItems() {
        return ApiResponse.success(adminConfigService.listServiceItems());
    }

    @GetMapping("/api/admin/catalog/service-items/{id}")
    @Operation(summary = "查询服务项详情")
    public ApiResponse<AdminConfigDtos.ServiceItemPayload> serviceItem(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getServiceItem(id));
    }

    @PostMapping("/api/admin/catalog/service-items")
    @Operation(summary = "新增服务项")
    public ApiResponse<AdminConfigDtos.ServiceItemPayload> createServiceItem(@RequestBody AdminConfigDtos.ServiceItemPayload payload) {
        return ApiResponse.success(adminConfigService.saveServiceItem(null, payload));
    }

    @PutMapping("/api/admin/catalog/service-items/{id}")
    @Operation(summary = "更新服务项")
    public ApiResponse<AdminConfigDtos.ServiceItemPayload> updateServiceItem(@PathVariable Long id,
                                                                             @RequestBody AdminConfigDtos.ServiceItemPayload payload) {
        return ApiResponse.success(adminConfigService.saveServiceItem(id, payload));
    }

    @DeleteMapping("/api/admin/catalog/service-items/{id}")
    @Operation(summary = "删除服务项")
    public ApiResponse<Boolean> deleteServiceItem(@PathVariable Long id) {
        adminConfigService.deleteServiceItem(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/catalog/products")
    @Operation(summary = "查询商品列表")
    public ApiResponse<List<AdminConfigDtos.ProductPayload>> products() {
        return ApiResponse.success(adminConfigService.listProducts());
    }

    @GetMapping("/api/admin/catalog/products/{id}")
    @Operation(summary = "查询商品详情")
    public ApiResponse<AdminConfigDtos.ProductPayload> product(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getProduct(id));
    }

    @PostMapping("/api/admin/catalog/products")
    @Operation(summary = "新增商品")
    public ApiResponse<AdminConfigDtos.ProductPayload> createProduct(@RequestBody AdminConfigDtos.ProductPayload payload) {
        return ApiResponse.success(adminConfigService.saveProduct(null, payload));
    }

    @PutMapping("/api/admin/catalog/products/{id}")
    @Operation(summary = "更新商品")
    public ApiResponse<AdminConfigDtos.ProductPayload> updateProduct(@PathVariable Long id,
                                                                     @RequestBody AdminConfigDtos.ProductPayload payload) {
        return ApiResponse.success(adminConfigService.saveProduct(id, payload));
    }

    @DeleteMapping("/api/admin/catalog/products/{id}")
    @Operation(summary = "删除商品")
    public ApiResponse<Boolean> deleteProduct(@PathVariable Long id) {
        adminConfigService.deleteProduct(id);
        return ApiResponse.success(true);
    }

    @GetMapping("/api/admin/catalog/skus")
    @Operation(summary = "查询 SKU 列表")
    public ApiResponse<List<AdminConfigDtos.SkuPayload>> skus() {
        return ApiResponse.success(adminConfigService.listSkus());
    }

    @GetMapping("/api/admin/catalog/skus/{id}")
    @Operation(summary = "查询 SKU 详情")
    public ApiResponse<AdminConfigDtos.SkuPayload> sku(@PathVariable Long id) {
        return ApiResponse.success(adminConfigService.getSku(id));
    }

    @PostMapping("/api/admin/catalog/skus")
    @Operation(summary = "新增 SKU")
    public ApiResponse<AdminConfigDtos.SkuPayload> createSku(@RequestBody AdminConfigDtos.SkuPayload payload) {
        return ApiResponse.success(adminConfigService.saveSku(null, payload));
    }

    @PutMapping("/api/admin/catalog/skus/{id}")
    @Operation(summary = "更新 SKU")
    public ApiResponse<AdminConfigDtos.SkuPayload> updateSku(@PathVariable Long id,
                                                             @RequestBody AdminConfigDtos.SkuPayload payload) {
        return ApiResponse.success(adminConfigService.saveSku(id, payload));
    }

    @DeleteMapping("/api/admin/catalog/skus/{id}")
    @Operation(summary = "删除 SKU")
    public ApiResponse<Boolean> deleteSku(@PathVariable Long id) {
        adminConfigService.deleteSku(id);
        return ApiResponse.success(true);
    }
}
