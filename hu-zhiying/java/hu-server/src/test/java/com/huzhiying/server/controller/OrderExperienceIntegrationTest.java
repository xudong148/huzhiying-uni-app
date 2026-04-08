package com.huzhiying.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderExperienceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeRealCommentsAndTracking() throws Exception {
        mockMvc.perform(get("/api/services/201/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].user").isNotEmpty());

        mockMvc.perform(get("/api/orders/SO20260408001/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.points").isArray());
    }

    @Test
    void shouldAcceptUrgeOrderRequest() throws Exception {
        mockMvc.perform(post("/api/orders/SO20260408001/urge")
                        .contentType("application/json")
                        .content("""
                                {
                                  "remark": "请尽快联系我"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.remark").value("请尽快联系我"));
    }
}
