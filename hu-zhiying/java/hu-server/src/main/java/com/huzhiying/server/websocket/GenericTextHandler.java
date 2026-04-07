package com.huzhiying.server.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GenericTextHandler extends TextWebSocketHandler {

    private final String channel;

    public GenericTextHandler(String channel) {
        this.channel = channel;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("[" + channel + "] " + message.getPayload()));
    }
}
