package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.savedChat.SavedChatCreatedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatDeletedEvent;
import com.burkina.messenger.service.ChatMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavedChatConsumer {

    private final ObjectMapper objectMapper;
    private final ChatMemberService memberService;

    @KafkaListener(topics = "saved-chat-created")
    public void handleSavedChatCreated(String message, Acknowledgment ack) throws JsonProcessingException {
        SavedChatCreatedEvent event = objectMapper.readValue(message, SavedChatCreatedEvent.class);
        memberService.createMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "saved-chat-deleted")
    public void handleSavedChatDeleted(String message, Acknowledgment ack) throws JsonProcessingException {
        SavedChatDeletedEvent event = objectMapper.readValue(message, SavedChatDeletedEvent.class);
        memberService.removeMembers(event);

        ack.acknowledge();
    }
}
