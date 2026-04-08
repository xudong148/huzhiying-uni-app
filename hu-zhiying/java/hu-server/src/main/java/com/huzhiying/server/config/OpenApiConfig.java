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
 * OpenAPI 分组文档配置，便于在 Knife4j、Swagger UI 和 Apifox 中按端查看接口。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI huZhiYingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("呼之应开放接口")
                        .version("v1")
                        .description("三端统一业务接口文档，覆盖 C 端、师傅端、后台和支付能力。"))
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
                        "/api/auth/**",
                        "/api/users/**",
                        "/api/addresses/**",
                        "/api/categories/**",
                        "/api/services/**",
                        "/api/products/**",
                        "/api/search/**",
                        "/api/service-orders/**",
                        "/api/product-orders/**",
                        "/api/quotations/**",
                        "/api/coupons/**",
                        "/api/members/**",
                        "/api/messages/**",
                        "/api/map/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi masterOpenApi() {
        return GroupedOpenApi.builder()
                .group("master")
                .pathsToMatch("/api/master/**", "/api/dispatch/**", "/api/notifications/**")
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
