package com.huzhiying.server.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GenericTextHandler extends TextWebSocketHandler {

    private final WebSocketEventGateway webSocketEventGateway;

    public GenericTextHandler(WebSocketEventGateway webSocketEventGateway) {
        this.webSocketEventGateway = webSocketEventGateway;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketEventGateway.register(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketEventGateway.unregister(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        webSocketEventGateway.sendPong(session, message.getPayload());
    }
}
