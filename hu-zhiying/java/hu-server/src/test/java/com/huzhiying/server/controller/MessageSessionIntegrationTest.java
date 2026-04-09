package com.huzhiying.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MessageSessionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeUnreadSummaryAndSupportMarkRead() throws Exception {
        mockMvc.perform(get("/api/messages/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[?(@.sessionId=='MS-001')].latestMessage").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.sessionId=='MS-001')].unreadCount").value(hasItem(greaterThanOrEqualTo(1))));

        mockMvc.perform(post("/api/messages/MS-001/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sessionId").value("MS-001"))
                .andExpect(jsonPath("$.data.unreadCount").value(0));

        mockMvc.perform(get("/api/messages/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.sessionId=='MS-001')].unreadCount").value(hasItem(0)));

        mockMvc.perform(get("/api/messages/sessions")
                        .header("Authorization", "Bearer token-20001-master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[?(@.sessionId=='MS-001')].participant").isNotEmpty());
    }
}
