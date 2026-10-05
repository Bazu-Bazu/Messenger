package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.personalChat.PersonalChatCreatedEvent;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatDeletedEvent;
import com.burkina.messenger.service.ChatMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalChatConsumer {

    private final ObjectMapper objectMapper;
    private final ChatMemberService memberService;

    @KafkaListener(topics = "personal-chat-created")
    public void handlePersonalChatCreated(String message, Acknowledgment ack) throws JsonProcessingException {
        PersonalChatCreatedEvent event = objectMapper.readValue(message, PersonalChatCreatedEvent.class);
        memberService.createMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "personal-chat-deleted")
    public void handlePersonalChatDeleted(String message, Acknowledgment ack) throws JsonProcessingException {
        PersonalChatDeletedEvent event = objectMapper.readValue(message, PersonalChatDeletedEvent.class);
        memberService.removeMembers(event);

        ack.acknowledge();
    }
}
