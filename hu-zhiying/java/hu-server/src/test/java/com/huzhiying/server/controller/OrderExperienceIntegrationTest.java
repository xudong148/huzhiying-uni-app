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
    void shouldExposeHomeAggregate() throws Exception {
        mockMvc.perform(get("/api/home"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hotKeywords").isArray())
                .andExpect(jsonPath("$.data.categoryNav").isArray())
                .andExpect(jsonPath("$.data.recommendations").isArray());
    }

    @Test
    void shouldExposeRealCommentsAndTracking() throws Exception {
        mockMvc.perform(get("/api/services/201/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].user").isNotEmpty());

        mockMvc.perform(get("/api/orders/SO20260408001/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.distance").isNotEmpty())
                .andExpect(jsonPath("$.data.points").isArray())
                .andExpect(jsonPath("$.data.mediaFiles").isArray())
                .andExpect(jsonPath("$.data.mediaFiles[0].bizType").value("order_evidence"));

        mockMvc.perform(get("/api/service-orders/SO20260408001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("SO20260408001"))
                .andExpect(jsonPath("$.data.timeline").isArray())
                .andExpect(jsonPath("$.data.messageSummary.sessionId").value("MS-002"))
                .andExpect(jsonPath("$.data.mediaFiles[0].bizType").value("order_evidence"))
                .andExpect(jsonPath("$.data.canUrge").value(true));
    }

    @Test
    void shouldExposeRichServiceOrderAggregate() throws Exception {
        mockMvc.perform(get("/api/service-orders/SO20260407009"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("SO20260407009"))
                .andExpect(jsonPath("$.data.quotation.id").value("QT20260408001"))
                .andExpect(jsonPath("$.data.messageSummary.sessionId").value("MS-001"))
                .andExpect(jsonPath("$.data.mediaFiles[0].bizType").value("before_work_media"))
                .andExpect(jsonPath("$.data.canPay").value(true));
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
