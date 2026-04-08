package com.huzhiying.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AdminConfigControllerIntegrationTest extends AdminControllerIntegrationSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldManageCatalogCrudAndBlockReferencedDelete() throws Exception {
        mockMvc.perform(admin(get("/api/admin/catalog/categories")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(admin(get("/api/admin/catalog/categories/2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(2));

        mockMvc.perform(admin(post("/api/admin/catalog/categories"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Temporary Category",
                                  "icon": "/icons/temp.svg",
                                  "sortOrder": 88,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Temporary Category"));

        mockMvc.perform(admin(put("/api/admin/catalog/categories/5"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Temporary Category Updated",
                                  "icon": "/icons/temp-2.svg",
                                  "sortOrder": 89,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Temporary Category Updated"));

        mockMvc.perform(admin(delete("/api/admin/catalog/categories/5")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(admin(delete("/api/admin/catalog/categories/2")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").isNotEmpty());

        mockMvc.perform(admin(post("/api/admin/catalog/service-items"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "categoryId": 99999,
                                  "name": "Broken Item",
                                  "basePrice": 10,
                                  "doorPrice": 10,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(admin(delete("/api/admin/catalog/service-items/201")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(admin(delete("/api/admin/catalog/products/1001")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldManageContentCrudAndValidatePayloads() throws Exception {
        mockMvc.perform(admin(get("/api/admin/content/banners")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(admin(get("/api/admin/content/banners/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(admin(post("/api/admin/content/banners"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Campaign Banner",
                                  "subtitle": "campaign",
                                  "image": "https://cdn.example.com/banner-campaign.jpg",
                                  "link": "/pages/campaign",
                                  "sortOrder": 55,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Campaign Banner"));

        mockMvc.perform(admin(put("/api/admin/content/banners/2"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Campaign Banner Updated",
                                  "subtitle": "campaign-updated",
                                  "image": "https://cdn.example.com/banner-campaign-2.jpg",
                                  "link": "/pages/campaign-updated",
                                  "sortOrder": 56,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Campaign Banner Updated"));

        mockMvc.perform(admin(delete("/api/admin/content/banners/2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(admin(post("/api/admin/content/notices"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Bad Notice",
                                  "content": "bad",
                                  "levelCode": "other",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("公告级别")));

        mockMvc.perform(admin(post("/api/admin/content/agreements"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "用户服务协议",
                                  "version": "2026.04",
                                  "content": "duplicate",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldManageMarketingCrudAndValidateCouponDate() throws Exception {
        mockMvc.perform(admin(get("/api/admin/marketing/coupons")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(admin(get("/api/admin/marketing/member-levels/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(admin(post("/api/admin/marketing/coupons"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Flash Coupon",
                                  "amount": 18,
                                  "thresholdText": "满 88 减 18",
                                  "expireAt": "2026-06-30",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Flash Coupon"));

        mockMvc.perform(admin(put("/api/admin/marketing/coupons/3"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Flash Coupon Updated",
                                  "amount": 20,
                                  "thresholdText": "满 99 减 20",
                                  "expireAt": "2026-07-01",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Flash Coupon Updated"));

        mockMvc.perform(admin(delete("/api/admin/marketing/coupons/3")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(admin(post("/api/admin/marketing/coupons"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Broken Coupon",
                                  "amount": 18,
                                  "thresholdText": "满 88 减 18",
                                  "expireAt": "2026/06/30",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("yyyy-MM-dd")));

        mockMvc.perform(admin(post("/api/admin/marketing/member-levels"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Duplicate Level",
                                  "benefitText": "duplicate",
                                  "pointsRequired": 2000,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldManageSystemCrudAndValidateCodes() throws Exception {
        mockMvc.perform(admin(get("/api/admin/system/roles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(admin(get("/api/admin/system/permissions/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(admin(post("/api/admin/system/roles"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "code": "ops_manager",
                                  "name": "Ops Manager",
                                  "description": "ops",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").value("ops_manager"));

        mockMvc.perform(admin(put("/api/admin/system/roles/4"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "code": "ops_manager",
                                  "name": "Ops Manager Updated",
                                  "description": "ops-updated",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Ops Manager Updated"));

        mockMvc.perform(admin(delete("/api/admin/system/roles/4")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(admin(post("/api/admin/system/permissions"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "code": "BAD_CODE",
                                  "name": "Broken Permission",
                                  "description": "broken",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("module:action")));

        mockMvc.perform(admin(post("/api/admin/system/menus"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Duplicate Menu",
                                  "path": "/dashboard",
                                  "icon": "mdi:view-dashboard",
                                  "sortOrder": 999,
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldManageRoleGrantsAndProtectBoundMenuAndPermissionDelete() throws Exception {
        mockMvc.perform(admin(get("/api/admin/system/roles/1/grants")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.roleId").value(1))
                .andExpect(jsonPath("$.data.menuIds").isArray())
                .andExpect(jsonPath("$.data.permissionIds").isArray());

        mockMvc.perform(admin(delete("/api/admin/system/menus/1")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(admin(delete("/api/admin/system/permissions/1")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldManageProductAndSkuPricingFields() throws Exception {
        MvcResult productCreateResult = mockMvc.perform(admin(post("/api/admin/catalog/products"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Smart Sensor Combo",
                                  "descriptionText": "智能家居套装",
                                  "tagPrice": 399.00,
                                  "discountPrice": 329.00,
                                  "price": 299.00,
                                  "createInstallOrder": true,
                                  "imageUrl": "/seed-media/product-sensor.svg",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tagPrice").value(399.00))
                .andExpect(jsonPath("$.data.discountPrice").value(329.00))
                .andExpect(jsonPath("$.data.price").value(299.00))
                .andReturn();

        long productId = responseDataId(productCreateResult);

        mockMvc.perform(admin(get("/api/admin/catalog/products/" + productId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.tagPrice").value(399.00))
                .andExpect(jsonPath("$.data.discountPrice").value(329.00))
                .andExpect(jsonPath("$.data.price").value(299.00));

        MvcResult skuCreateResult = mockMvc.perform(admin(post("/api/admin/catalog/skus"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "productId": %d,
                                  "name": "标准版",
                                  "tagPrice": 399.00,
                                  "discountPrice": 319.00,
                                  "price": 299.00,
                                  "stock": 24,
                                  "enabled": true
                                }
                                """.formatted(productId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.stock").value(24))
                .andReturn();

        long skuId = responseDataId(skuCreateResult);

        mockMvc.perform(admin(put("/api/admin/catalog/skus/" + skuId))
                        .contentType("application/json")
                        .content("""
                                {
                                  "productId": %d,
                                  "name": "标准版升级",
                                  "tagPrice": 429.00,
                                  "discountPrice": 339.00,
                                  "price": 309.00,
                                  "stock": 30,
                                  "enabled": true
                                }
                                """.formatted(productId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tagPrice").value(429.00))
                .andExpect(jsonPath("$.data.discountPrice").value(339.00))
                .andExpect(jsonPath("$.data.price").value(309.00))
                .andExpect(jsonPath("$.data.stock").value(30));

        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.tagPrice").value(399.00))
                .andExpect(jsonPath("$.data.discountPrice").value(329.00))
                .andExpect(jsonPath("$.data.skus[0].tagPrice").value(429.00))
                .andExpect(jsonPath("$.data.skus[0].discountPrice").value(339.00))
                .andExpect(jsonPath("$.data.skus[0].stock").value(30));
    }

    @Test
    void shouldManagePricingCrudAndProtectDispatchZones() throws Exception {
        mockMvc.perform(admin(get("/api/admin/pricing/rules")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(admin(get("/api/admin/dispatch/zones/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));

        mockMvc.perform(admin(post("/api/admin/pricing/rules"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "categoryId": 2,
                                  "label": "Weekend Rule",
                                  "basePrice": 66,
                                  "coefficient": "周末 +10%",
                                  "guidePrice": "66 - 199",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.label").value("Weekend Rule"));

        mockMvc.perform(admin(put("/api/admin/pricing/rules/4"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "categoryId": 2,
                                  "label": "Weekend Rule Updated",
                                  "basePrice": 68,
                                  "coefficient": "周末 +12%",
                                  "guidePrice": "68 - 199",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.label").value("Weekend Rule Updated"));

        mockMvc.perform(admin(delete("/api/admin/pricing/rules/4")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(admin(post("/api/admin/pricing/rules"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "categoryId": 2,
                                  "label": "空调维修基础价",
                                  "basePrice": 58,
                                  "coefficient": "duplicate",
                                  "guidePrice": "58 - 299",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(admin(put("/api/admin/dispatch/zones/2"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "cityName": "上海",
                                  "districtName": "徐汇区",
                                  "sortOrder": 20,
                                  "enabled": false
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(admin(put("/api/admin/dispatch/zones/3"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "cityName": "杭州",
                                  "districtName": "西湖区",
                                  "sortOrder": 30,
                                  "enabled": false
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(admin(put("/api/admin/dispatch/zones/4"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "cityName": "苏州",
                                  "districtName": "工业园区",
                                  "sortOrder": 40,
                                  "enabled": false
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(admin(put("/api/admin/dispatch/zones/1"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "cityName": "上海",
                                  "districtName": "浦东新区",
                                  "sortOrder": 10,
                                  "enabled": false
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    private long responseDataId(MvcResult result) throws Exception {
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }
}
