package com.huzhiying.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzhiying.domain.enums.DomainEnums.NotificationTaskStatus;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.service.PlatformCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminOverviewControllerIntegrationTest extends AdminControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlatformCommandService commandService;

    @Autowired
    private PlatformRepository platformRepository;

    @Test
    void shouldReturnTypedFinanceRows() throws Exception {
        MvcResult result = mockMvc.perform(admin(get("/api/admin/finance")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        JsonNode rows = objectMapper.readTree(result.getResponse().getContentAsString()).path("data");
        assertTrue(rows.isArray());
        assertTrue(rows.size() > 0);
        assertTrue(StreamSupport.stream(rows.spliterator(), false)
                .anyMatch(item -> !item.path("billNo").asText().isBlank()
                        && !item.path("type").asText().isBlank()
                        && !item.path("status").asText().isBlank()));
    }

    @Test
    void shouldListAndDispatchNotificationTasks() throws Exception {
        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "notification dispatch test",
                "2026-04-10 10:00-12:00",
                1L,
                "controller notification test",
                false,
                false,
                List.of()
        );
        commandService.handleWechatCallback(created.id());

        var notificationTask = platformRepository.listNotificationTasksByBiz("SERVICE", created.id()).stream()
                .filter(item -> "payment-success".equals(item.templateCode))
                .reduce((first, second) -> second)
                .orElseThrow();

        MvcResult listResult = mockMvc.perform(admin(get("/api/admin/notifications")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items").isArray())
                .andReturn();

        JsonNode items = objectMapper.readTree(listResult.getResponse().getContentAsString())
                .path("data")
                .path("items");
        JsonNode matched = StreamSupport.stream(items.spliterator(), false)
                .filter(item -> notificationTask.bizNo.equals(item.path("bizNo").asText()))
                .findFirst()
                .orElse(null);
        assertNotNull(matched);
        assertTrue("PENDING".equals(matched.path("status").asText()) || "FAILED".equals(matched.path("status").asText()));

        MvcResult dispatchResult = mockMvc.perform(admin(post("/api/admin/notifications/dispatch")
                        .param("limit", "500")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        JsonNode dispatchData = objectMapper.readTree(dispatchResult.getResponse().getContentAsString()).path("data");
        assertTrue(dispatchData.path("processedCount").asInt() >= 1);
        assertTrue(dispatchData.path("successCount").asInt() >= 1);

        var refreshedTask = platformRepository.listNotificationTasksByBiz("SERVICE", created.id()).stream()
                .filter(item -> notificationTask.bizNo.equals(item.bizNo))
                .findFirst()
                .orElseThrow();
        assertTrue(refreshedTask.status == NotificationTaskStatus.SENT
                || refreshedTask.status == NotificationTaskStatus.FAILED);
    }
}
