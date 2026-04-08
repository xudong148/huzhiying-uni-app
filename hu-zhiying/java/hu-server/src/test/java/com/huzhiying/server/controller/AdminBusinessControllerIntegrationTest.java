package com.huzhiying.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminBusinessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetDispatchDetail() throws Exception {
        mockMvc.perform(get("/api/admin/dispatch/DISP-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.taskId").value("DISP-001"))
                .andExpect(jsonPath("$.data.orderId").value("SO20260408001"));
    }

    @Test
    void shouldAssignDispatchTask() throws Exception {
        mockMvc.perform(post("/api/admin/dispatch/DISP-001/assign")
                        .contentType("application/json")
                        .content("""
                                {
                                  "masterName": "张师傅"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.masterName").value("张师傅"));
    }

    @Test
    void shouldGetOrderDetail() throws Exception {
        mockMvc.perform(get("/api/admin/orders/SO20260408001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value("SO20260408001"))
                .andExpect(jsonPath("$.data.orderType").value("SERVICE"));
    }

    @Test
    void shouldDisableMaster() throws Exception {
        mockMvc.perform(post("/api/admin/masters/20001/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.enabled").value(false));
    }

    @Test
    void shouldApproveSettlementBill() throws Exception {
        mockMvc.perform(post("/api/admin/finance/SETTLE-1/approve")
                        .contentType("application/json")
                        .content("""
                                {
                                  "remark": "财务审核通过"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("已结算"));
    }

    @Test
    void shouldResolveArbitration() throws Exception {
        mockMvc.perform(post("/api/admin/arbitrations/ARB-001/resolve")
                        .contentType("application/json")
                        .content("""
                                {
                                  "statusText": "已裁决",
                                  "resultText": "判定退还增项费用"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("已裁决"))
                .andExpect(jsonPath("$.data.result").value("判定退还增项费用"));
    }

    @Test
    void shouldUpdateMasterDetail() throws Exception {
        mockMvc.perform(put("/api/admin/masters/20001")
                        .contentType("application/json")
                        .content("""
                                {
                                  "realName": "张师傅",
                                  "serviceAreas": ["浦东新区", "徐汇区"],
                                  "skillTags": ["空调维修", "智能锁安装"],
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.realName").value("张师傅"));
    }
}
