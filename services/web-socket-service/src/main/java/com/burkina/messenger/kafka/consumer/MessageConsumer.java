package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.message.MessageReadEvent;
import com.burkina.common.dto.event.messenger.message.MessageRemovedEvent;
import com.burkina.common.dto.event.messenger.message.MessageSentEvent;
import com.burkina.messenger.dto.response.MessageReadResponse;
import com.burkina.messenger.dto.response.MessageRemovedResponse;
import com.burkina.messenger.dto.response.MessageSentResponse;
import com.burkina.messenger.mapper.MessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.burkina.messenger.service.SessionManagerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageConsumer {

    private final ObjectMapper objectMapper;
    private final MessageMapper messageMapper;
    private final SessionManagerService sessionManagerService;

    @KafkaListener(topics = "messages-sent-event")
    public void handleMessageSentEvent(String message, Acknowledgment ack) throws IOException {
        MessageSentEvent event = objectMapper.readValue(message, MessageSentEvent.class);

        MessageSentResponse response = messageMapper.toMessageSentResponse(event);
        String json = objectMapper.writeValueAsString(response);

        Set<WebSocketSession> sessions = sessionManagerService.getSessionsByUsersId(event.userIds());
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Sending message to session {}", session.getId());
                session.sendMessage(new TextMessage(json));
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "message-read-event")
    public void handleMessageReadEvent(String message, Acknowledgment ack) throws IOException {
        MessageReadEvent event = objectMapper.readValue(message, MessageReadEvent.class);

        MessageReadResponse response = messageMapper.toMessageReadResponse(event);
        String json = objectMapper.writeValueAsString(response);

        Set<WebSocketSession> sessions = sessionManagerService.getSessionsByUsersId(event.userIds());
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Reading message to session {}", session.getId());
                session.sendMessage(new TextMessage(json));
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "message-removed-event")
    public void handleMessageRemovedEvent(String message, Acknowledgment ack) throws IOException {
        MessageRemovedEvent event = objectMapper.readValue(message, MessageRemovedEvent.class);

        MessageRemovedResponse response = messageMapper.toMessageRemovedResponse(event);
        String json = objectMapper.writeValueAsString(response);

        Set<WebSocketSession> sessions = sessionManagerService.getSessionsByUsersId(event.userIds());
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Removing message to session {}", session.getId());
                session.sendMessage(new TextMessage(json));
            }
        }

        ack.acknowledge();
    }
}
