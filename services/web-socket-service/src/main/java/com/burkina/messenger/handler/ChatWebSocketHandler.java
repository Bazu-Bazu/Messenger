package com.burkina.messenger.handler;

import com.burkina.messenger.dto.request.MarkMessageAsReadRequest;
import com.burkina.messenger.dto.request.RemoveMessageRequest;
import com.burkina.messenger.dto.request.SendMessageRequest;
import com.burkina.messenger.mapper.MessageMapper;
import com.burkina.messenger.service.event.MessagePublisher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.service.SessionManagerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageMapper messageMapper;
    private final MessagePublisher messagePublisher;
    private final SessionManagerService sessionManagerService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("USER_ID");
        if (userId != null) {
            sessionManagerService.addSession(session, userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("USER_ID");
        if (userId == null) {
            sendResult(session, false);
            return;
        }

        try {
            JsonNode root = objectMapper.readTree(message.getPayload());
            String action = root.path("action").asText(null);

            switch (action) {
                case "send" -> handleSend(root, userId);
                case "read" -> handleRead(root, userId);
                case "remove" -> handleRemove(root, userId);
                default -> throw new IllegalArgumentException("Unknown action");
            };

            sendResult(session, true);
        } catch (Exception e) {
            sendResult(session, false);
        }
    }

    private void handleSend(JsonNode payload, Long userId) {
        SendMessageRequest request = objectMapper.convertValue(payload, SendMessageRequest.class);
        request.validate();
        messagePublisher.publishSendMessageEvent(messageMapper.toSendMessageEvent(request, userId));
    }

    private void handleRead(JsonNode payload, Long userId) {
        MarkMessageAsReadRequest request = objectMapper.convertValue(payload, MarkMessageAsReadRequest.class);
        messagePublisher.publishMarkMessageAsReadEvent(messageMapper.toMarkMessageAsReadEvent(request, userId));
    }

    private void handleRemove(JsonNode payload, Long userId) {
        RemoveMessageRequest request = objectMapper.convertValue(payload, RemoveMessageRequest.class);
        messagePublisher.publishRemoveMessageEvent(messageMapper.toRemoveMessageEvent(request, userId));
    }

    private void sendResult(WebSocketSession session, boolean result) throws IOException {
        session.sendMessage(
                new TextMessage(
                        objectMapper.writeValueAsString(result)
                )
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManagerService.removeSession(session.getId());
    }
}
