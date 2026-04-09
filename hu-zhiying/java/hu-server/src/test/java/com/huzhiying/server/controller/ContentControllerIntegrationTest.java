package com.huzhiying.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContentControllerIntegrationTest extends AdminControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnProductionHomePayload() throws Exception {
        mockMvc.perform(get("/api/home"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.ecosystemCards[0].link").isNotEmpty())
                .andExpect(jsonPath("$.data.categoryNav").isArray())
                .andExpect(jsonPath("$.data.recommendations").isArray());
    }

    @Test
    void shouldReadAcademyContent() throws Exception {
        mockMvc.perform(get("/api/academy/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").isNotEmpty());

        mockMvc.perform(get("/api/academy/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").isNotEmpty());

        mockMvc.perform(get("/api/academy/articles/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").isNotEmpty())
                .andExpect(jsonPath("$.data.content").isNotEmpty());
    }

    @Test
    void shouldCreateLikeCommentAndReportCommunityPost() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/community/posts")
                        .contentType("application/json")
                        .content("""
                                {
                                  "cityName": "上海",
                                  "title": "测试帖子",
                                  "content": "这是一个用于集成测试的圈子帖子。",
                                  "images": ["https://example.com/test-post.jpg"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("测试帖子"))
                .andReturn();

        Long postId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data")
                .path("id")
                .asLong();

        mockMvc.perform(post("/api/community/posts/" + postId + "/comments")
                        .contentType("application/json")
                        .content("""
                                {
                                  "content": "评论一下，确认链路可用。"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.postId").value(postId));

        mockMvc.perform(post("/api/community/posts/" + postId + "/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.likeCount").value(1));

        mockMvc.perform(post("/api/community/posts/" + postId + "/report")
                        .contentType("application/json")
                        .content("""
                                {
                                  "reason": "测试举报",
                                  "detail": "用于验证举报接口"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.postId").value(postId))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void shouldManageProductionContentInAdmin() throws Exception {
        mockMvc.perform(admin(post("/api/admin/content/ecosystem-cards"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "运维公告",
                                  "description": "测试新增生态卡",
                                  "icon": "https://example.com/icon.svg",
                                  "color": "#123456",
                                  "link": "/pages/academy/index",
                                  "sortOrder": 99,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("运维公告"));

        mockMvc.perform(admin(get("/api/admin/content/community-reports")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}
