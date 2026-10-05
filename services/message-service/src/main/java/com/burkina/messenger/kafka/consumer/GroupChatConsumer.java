package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.groupChat.*;
import com.burkina.messenger.service.ChatMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupChatConsumer {

    private final ObjectMapper objectMapper;
    private final ChatMemberService memberService;

    @KafkaListener(topics = "group-chat-created")
    public void handleGroupChatCreated(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatCreatedEvent event = objectMapper.readValue(message, GroupChatCreatedEvent.class);
        memberService.createMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-deleted")
    public void handleGroupChatDeleted(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatDeletedEvent event = objectMapper.readValue(message, GroupChatDeletedEvent.class);
        memberService.removeMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-updated")
    public void handleGroupChatMembersUpdated(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersUpdatedEvent event = objectMapper.readValue(message, GroupChatMembersUpdatedEvent.class);
        memberService.updateMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-deleted")
    public void handleGroupChatMembersDeleted(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersDeletedEvent event = objectMapper.readValue(message, GroupChatMembersDeletedEvent.class);
        memberService.removeMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-added")
    public void handleGroupChatMembersAdded(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersAddedEvent event = objectMapper.readValue(message, GroupChatMembersAddedEvent.class);
        memberService.createMembers(event);

        ack.acknowledge();
    }
}
