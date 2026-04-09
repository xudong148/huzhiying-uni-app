package com.huzhiying.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminOpenApiIntegrationTest extends AdminControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeAdminOpenApiGroup() throws Exception {
        mockMvc.perform(get("/v3/api-docs/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths").exists())
                .andExpect(jsonPath("$.paths['/api/admin/catalog/categories']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/pricing/rules']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/banners']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/ecosystem-cards']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/academy-categories']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/academy-articles']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/community-posts']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/community-reports']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/marketing/coupons']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/system/roles']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/system/roles/{id}/grants']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/system/permissions']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/dispatch/{taskId}']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/orders/{orderId}']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/masters/{userId}']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/orders/{orderId}/grant-coupon']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/orders/{orderId}/appointment']").exists());
    }

    @Test
    void shouldExposeMobileOpenApiGroup() throws Exception {
        mockMvc.perform(get("/v3/api-docs/mobile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/home']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/admin-login']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/send-code']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/mobile-login']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/register']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/refresh']").exists())
                .andExpect(jsonPath("$.paths['/api/academy/categories']").exists())
                .andExpect(jsonPath("$.paths['/api/academy/articles']").exists())
                .andExpect(jsonPath("$.paths['/api/academy/articles/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/community/posts']").exists())
                .andExpect(jsonPath("$.paths['/api/community/posts/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/community/posts/{id}/comments']").exists())
                .andExpect(jsonPath("$.paths['/api/community/posts/{id}/like']").exists())
                .andExpect(jsonPath("$.paths['/api/community/posts/{id}/report']").exists())
                .andExpect(jsonPath("$.paths['/api/orders/{id}/tracking']").exists())
                .andExpect(jsonPath("$.paths['/api/files/upload']").exists());
    }

    @Test
    void shouldExposeKnife4jDocumentPage() throws Exception {
        mockMvc.perform(get("/doc.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("knife4j")));

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doc.html"));
    }

    @Test
    void shouldCreateBannerThroughAdminCrudApi() throws Exception {
        mockMvc.perform(admin(post("/api/admin/content/banners"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "新建 Banner",
                                  "subtitle": "用于测试后台 CRUD",
                                  "image": "https://cdn.example.com/banner-test.jpg",
                                  "link": "/pages/index/index",
                                  "sortOrder": 99,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("新建 Banner"))
                .andExpect(jsonPath("$.data.sortOrder").value(99));
    }
}
