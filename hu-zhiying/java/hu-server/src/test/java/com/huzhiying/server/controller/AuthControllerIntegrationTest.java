package com.huzhiying.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginAdminReadSessionAndRefreshToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/admin-login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "Admin@123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("admin"))
                .andExpect(jsonPath("$.data.profile.username").value("admin"))
                .andExpect(jsonPath("$.data.menus[*].path", hasItem("/dashboard")))
                .andExpect(jsonPath("$.data.permissions", hasItem("content:publish")))
                .andReturn();

        JsonNode loginData = responseData(loginResult);
        String accessToken = loginData.path("token").asText();
        String refreshToken = loginData.path("refreshToken").asText();

        mockMvc.perform(get("/api/auth/session")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.profile.username").value("admin"))
                .andExpect(jsonPath("$.data.role").value("admin"))
                .andExpect(jsonPath("$.data.menus[*].path", hasItem("/system")))
                .andExpect(jsonPath("$.data.permissions", hasItem("dispatch:force-assign")));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType("application/json")
                        .content("""
                                {
                                  "refreshToken": "%s",
                                  "clientType": "admin-web"
                                }
                                """.formatted(refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.profile.username").value("admin"));
    }

    @Test
    void shouldSendSmsCodeRegisterAndLoginByMobile() throws Exception {
        mockMvc.perform(post("/api/auth/send-code")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13900009999",
                                  "purpose": "register"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.mobile").value("13900009999"))
                .andExpect(jsonPath("$.data.purpose").value("register"))
                .andExpect(jsonPath("$.data.expiresInSeconds").value(300));

        PersistenceEntities.SmsCodeEntity registerCode = platformRepository.findLatestActiveSmsCode("13900009999", "register")
                .orElseThrow();

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13900009999",
                                  "code": "%s",
                                  "nickname": "新用户"
                                }
                                """.formatted(registerCode.code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("user"))
                .andExpect(jsonPath("$.data.profile.mobile").value("13900009999"))
                .andExpect(jsonPath("$.data.profile.name").value("新用户"));

        mockMvc.perform(post("/api/auth/send-code")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13900001234",
                                  "purpose": "login"
                                }
                                """))
                .andExpect(status().isOk());

        PersistenceEntities.SmsCodeEntity userLoginCode = platformRepository.findLatestActiveSmsCode("13900001234", "login")
                .orElseThrow();

        mockMvc.perform(post("/api/auth/mobile-login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13900001234",
                                  "code": "%s",
                                  "role": "user"
                                }
                                """.formatted(userLoginCode.code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("user"))
                .andExpect(jsonPath("$.data.profile.mobile").value("13900001234"))
                .andExpect(jsonPath("$.data.menus").isArray())
                .andExpect(jsonPath("$.data.menus").isEmpty());

        mockMvc.perform(post("/api/auth/send-code")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13700004567",
                                  "purpose": "login"
                                }
                                """))
                .andExpect(status().isOk());

        PersistenceEntities.SmsCodeEntity masterLoginCode = platformRepository.findLatestActiveSmsCode("13700004567", "login")
                .orElseThrow();

        mockMvc.perform(post("/api/auth/mobile-login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13700004567",
                                  "code": "%s",
                                  "role": "master"
                                }
                                """.formatted(masterLoginCode.code)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("master"))
                .andExpect(jsonPath("$.data.profile.mobile").value("13700004567"));
    }

    @Test
    void shouldRejectInvalidAuthRequestsAndProtectAdminApis() throws Exception {
        mockMvc.perform(post("/api/auth/admin-login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(post("/api/auth/send-code")
                        .contentType("application/json")
                        .content("""
                                {
                                  "mobile": "13900001234",
                                  "purpose": "reset"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("login")));

        mockMvc.perform(get("/api/admin/catalog/categories"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    private JsonNode responseData(MvcResult result) throws Exception {
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data");
    }
}
