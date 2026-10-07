package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.personalChat.PersonalChatCreatedEvent;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatDeletedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.service.PersonalChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalChatConsumer {

    private final ObjectMapper mapper;
    private final PersonalChatService chatService;

    @KafkaListener(topics = "personal-chat-created")
    public void handlePersonalChatCreatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        PersonalChatCreatedEvent event = mapper.readValue(message, PersonalChatCreatedEvent.class);

        chatService.create(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "personal-chat-deleted")
    public void handlePersonalChatDeletedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        PersonalChatDeletedEvent event = mapper.readValue(message, PersonalChatDeletedEvent.class);

        chatService.delete(event);

        ack.acknowledge();
    }
}
