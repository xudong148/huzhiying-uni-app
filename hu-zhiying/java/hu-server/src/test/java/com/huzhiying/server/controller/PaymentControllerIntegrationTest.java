package com.huzhiying.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRejectPrepayWhenWechatConfigMissing() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/service-orders")
                        .contentType("application/json")
                        .content("""
                                {
                                  "serviceItemId": 201,
                                  "title": "空调上门维修",
                                  "appointment": "今天 18:00-20:00",
                                  "addressId": 1,
                                  "description": "室内机制冷效果差",
                                  "emergency": false,
                                  "nightService": false,
                                  "evidenceFileIds": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        String orderId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .path("data")
                .path("id")
                .asText();

        mockMvc.perform(post("/api/payments/wechat/prepay")
                        .contentType("application/json")
                        .content("{\"orderId\":\"" + orderId + "\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("当前环境未配置微信支付商户参数，无法创建预支付单。"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
