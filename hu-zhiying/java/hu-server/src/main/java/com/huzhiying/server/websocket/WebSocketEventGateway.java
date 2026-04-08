package com.huzhiying.server.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventGateway {

    private final ObjectMapper objectMapper;
    private final Map<String, Set<WebSocketSession>> topicSessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> sessionTopics = new ConcurrentHashMap<>();

    public WebSocketEventGateway(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void register(WebSocketSession session) throws IOException {
        List<String> topics = resolveTopics(session.getUri());
        if (topics.isEmpty()) {
            return;
        }

        sessionTopics.put(session.getId(), Set.copyOf(topics));
        topics.forEach(topic -> topicSessions.computeIfAbsent(topic, key -> ConcurrentHashMap.newKeySet()).add(session));

        send(session, "connected", Map.of("topics", topics));
    }

    public void unregister(WebSocketSession session) {
        Set<String> topics = sessionTopics.remove(session.getId());
        if (topics == null) {
            return;
        }
        topics.forEach(topic -> {
            Set<WebSocketSession> sessions = topicSessions.get(topic);
            if (sessions == null) {
                return;
            }
            sessions.remove(session);
            if (sessions.isEmpty()) {
                topicSessions.remove(topic);
            }
        });
    }

    public void publishDispatchUpdate(Object payload) {
        publish("dispatch", "dispatch_update", payload);
    }

    public void publishOrderStatusChanged(String orderId, Object payload) {
        publish("order:" + orderId, "order_status_changed", payload);
        publish("orders", "order_status_changed", payload);
    }

    public void publishQuotationCreated(String orderId, Object payload) {
        publish("order:" + orderId, "quotation_created", payload);
        publish("orders", "quotation_created", payload);
    }

    public void publishChatMessage(Object payload) {
        publish("chat", "chat_message", payload);
    }

    public void sendPong(WebSocketSession session, String payload) throws IOException {
        send(session, "pong", Map.of("payload", payload));
    }

    private void publish(String topic, String event, Object payload) {
        Set<WebSocketSession> sessions = topicSessions.get(topic);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        sessions.removeIf(session -> !session.isOpen());
        for (WebSocketSession session : sessions) {
            try {
                send(session, event, payload);
            } catch (IOException error) {
                unregister(session);
            }
        }
    }

    private void send(WebSocketSession session, String event, Object payload) throws IOException {
        if (!session.isOpen()) {
            return;
        }
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("event", event);
        message.put("payload", payload);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private List<String> resolveTopics(URI uri) {
        if (uri == null) {
            return List.of();
        }
        String path = uri.getPath();
        if (path == null || path.isBlank()) {
            return List.of();
        }
        if (path.endsWith("/ws/chat")) {
            return List.of("chat");
        }
        if (path.endsWith("/ws/dispatch")) {
            return List.of("dispatch");
        }
        if (path.endsWith("/ws/orders")) {
            return List.of("orders");
        }
        int index = path.indexOf("/ws/orders/");
        if (index >= 0) {
            String orderId = path.substring(index + "/ws/orders/".length()).trim();
            if (!orderId.isBlank()) {
                return List.of("orders", "order:" + orderId);
            }
        }
        return List.of();
    }
}
