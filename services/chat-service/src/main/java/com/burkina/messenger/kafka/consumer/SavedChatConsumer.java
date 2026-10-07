package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.savedChat.SavedChatCreatedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatDeletedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.service.SavedChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavedChatConsumer {

    private final ObjectMapper mapper;
    private final SavedChatService chatService;

    @KafkaListener(topics = "saved-chat-created")
    public void handleSavedChatCreatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        SavedChatCreatedEvent event = mapper.readValue(message, SavedChatCreatedEvent.class);

        chatService.create(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "saved-chat-deleted")
    public void handleSavedChatDeletedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        SavedChatDeletedEvent event = mapper.readValue(message, SavedChatDeletedEvent.class);

        chatService.delete(event);

        ack.acknowledge();
    }
}
