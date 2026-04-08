package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.service.PlatformFacadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private final PlatformFacadeService platformFacadeService;

    public CatalogController(PlatformFacadeService platformFacadeService) {
        this.platformFacadeService = platformFacadeService;
    }

    @GetMapping("/api/categories")
    public ApiResponse<?> categories() {
        return ApiResponse.success(platformFacadeService.categories());
    }

    @GetMapping("/api/services")
    public ApiResponse<?> services() {
        return ApiResponse.success(platformFacadeService.services());
    }

    @GetMapping("/api/services/{id}")
    public ApiResponse<?> serviceDetail(@PathVariable Long id) {
        return ApiResponse.success(platformFacadeService.serviceDetail(id));
    }

    @GetMapping("/api/products")
    public ApiResponse<?> products() {
        return ApiResponse.success(platformFacadeService.products());
    }

    @GetMapping("/api/products/{id}")
    public ApiResponse<?> productDetail(@PathVariable Long id) {
        return ApiResponse.success(platformFacadeService.productDetail(id));
    }

    @GetMapping("/api/search")
    public ApiResponse<?> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.success(platformFacadeService.search(keyword));
    }
}
