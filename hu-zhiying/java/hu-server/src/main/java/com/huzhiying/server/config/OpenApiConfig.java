package com.huzhiying.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 分组配置。
 * 1. 供 Knife4j 页面展示。
 * 2. 供 Apifox / Apiform 导入。
 * 3. 供 Markdown 脚本离线导出。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI huZhiYingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("呼之应开放接口文档")
                        .version("v1")
                        .description("呼之应平台内闭环接口契约，覆盖 C 端、师傅端、后台与支付流程。"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }

    @Bean
    public GroupedOpenApi mobileOpenApi() {
        return GroupedOpenApi.builder()
                .group("mobile")
                .pathsToMatch(
                        "/api/home",
                        "/api/auth/**",
                        "/api/users/**",
                        "/api/addresses/**",
                        "/api/categories/**",
                        "/api/services/**",
                        "/api/products/**",
                        "/api/search/**",
                        "/api/orders/**",
                        "/api/service-orders/**",
                        "/api/product-orders/**",
                        "/api/quotations/**",
                        "/api/academy/**",
                        "/api/community/**",
                        "/api/coupons/**",
                        "/api/members/**",
                        "/api/messages/**",
                        "/api/files/**",
                        "/api/map/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi masterOpenApi() {
        return GroupedOpenApi.builder()
                .group("master")
                .pathsToMatch(
                        "/api/master/**",
                        "/api/dispatch/**",
                        "/api/notifications/**",
                        "/api/service-orders/**",
                        "/api/orders/**",
                        "/api/quotations/**",
                        "/api/messages/**",
                        "/api/files/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi adminOpenApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentOpenApi() {
        return GroupedOpenApi.builder()
                .group("payment")
                .pathsToMatch("/api/payments/**")
                .build();
    }
}
