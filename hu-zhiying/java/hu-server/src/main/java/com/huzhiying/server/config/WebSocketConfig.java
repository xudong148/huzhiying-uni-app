package com.huzhiying.server.config;

import com.huzhiying.server.websocket.GenericTextHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GenericTextHandler("chat"), "/ws/chat").setAllowedOriginPatterns("*");
        registry.addHandler(new GenericTextHandler("dispatch"), "/ws/dispatch").setAllowedOriginPatterns("*");
        registry.addHandler(new GenericTextHandler("orders"), "/ws/orders").setAllowedOriginPatterns("*");
    }
}
