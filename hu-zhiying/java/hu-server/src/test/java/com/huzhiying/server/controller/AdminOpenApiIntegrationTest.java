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
class AdminOpenApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeAdminOpenApiGroup() throws Exception {
        mockMvc.perform(get("/v3/api-docs/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths").exists())
                .andExpect(jsonPath("$.paths['/api/admin/content/banners']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/dispatch/{taskId}']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/orders/{orderId}']").exists())
                .andExpect(jsonPath("$.paths['/api/admin/masters/{userId}']").exists());
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
        mockMvc.perform(post("/api/admin/content/banners")
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
