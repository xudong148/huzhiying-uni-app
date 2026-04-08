package com.huzhiying.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionIdentityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCurrentUserProfileByBearerToken() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer token-20001-master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.profile.id").value(20001))
                .andExpect(jsonPath("$.data.profile.nickname").isNotEmpty())
                .andExpect(jsonPath("$.data.profile.avatar").isNotEmpty())
                .andExpect(jsonPath("$.data.profile.level").isNotEmpty())
                .andExpect(jsonPath("$.data.profile.role").value("MASTER"));
    }

    @Test
    void shouldReturnMasterDashboardByBearerToken() throws Exception {
        mockMvc.perform(get("/api/master/dashboard")
                        .header("Authorization", "Bearer token-20001-master"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.wallet").exists())
                .andExpect(jsonPath("$.data.orders").isArray())
                .andExpect(jsonPath("$.data.settings").exists())
                .andExpect(jsonPath("$.data.offlineQueueSummary.pendingActions").exists())
                .andExpect(jsonPath("$.data.offlineQueueSummary.pendingUploads").exists());
    }

    @Test
    void shouldClaimDispatchTaskWithoutMasterNamePayload() throws Exception {
        mockMvc.perform(post("/api/dispatch/tasks/DISP-001/claim")
                        .header("Authorization", "Bearer token-20001-master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("DISP-001"))
                .andExpect(jsonPath("$.data.currentMaster").isNotEmpty());
    }
}
