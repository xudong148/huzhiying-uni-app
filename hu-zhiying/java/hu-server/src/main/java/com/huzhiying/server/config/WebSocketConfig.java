package com.huzhiying.server.config;

import com.huzhiying.server.websocket.GenericTextHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GenericTextHandler genericTextHandler;

    public WebSocketConfig(GenericTextHandler genericTextHandler) {
        this.genericTextHandler = genericTextHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(genericTextHandler, "/ws/chat", "/ws/dispatch", "/ws/orders", "/ws/orders/*")
                .setAllowedOriginPatterns("*");
    }
}
