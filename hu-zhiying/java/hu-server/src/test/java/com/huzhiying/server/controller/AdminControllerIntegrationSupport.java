package com.huzhiying.server.controller;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

abstract class AdminControllerIntegrationSupport {

    protected static final String ADMIN_AUTHORIZATION = "Bearer token-90001-admin";

    protected MockHttpServletRequestBuilder admin(MockHttpServletRequestBuilder builder) {
        return builder.header("Authorization", ADMIN_AUTHORIZATION);
    }
}
