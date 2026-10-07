package com.burkina.messenger.kafka.consumer;

import com.burkina.common.dto.event.messenger.groupChat.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.service.GroupChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupChatConsumer {

    private final ObjectMapper mapper;
    private final GroupChatService chatService;

    @KafkaListener(topics = "group-chat-created")
    public void handleGroupChatCreatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatCreatedEvent event = mapper.readValue(message, GroupChatCreatedEvent.class);

        chatService.create(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-deleted")
    public void handleGroupChatDeletedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatDeletedEvent event = mapper.readValue(message, GroupChatDeletedEvent.class);

        chatService.delete(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-info-updated")
    public void handleGroupChatInfoUpdatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatInfoUpdatedEvent event = mapper.readValue(message, GroupChatInfoUpdatedEvent.class);

        chatService.changeInfo(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-updated")
    public void handleGroupChatMembersUpdatedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersUpdatedEvent event = mapper.readValue(message, GroupChatMembersUpdatedEvent.class);

        chatService.changePermissions(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-deleted")
    public void handleGroupChatMembersDeletedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersDeletedEvent event = mapper.readValue(message, GroupChatMembersDeletedEvent.class);

        chatService.removeMembers(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "group-chat-members-added")
    public void handleGroupChatMembersAddedEvent(String message, Acknowledgment ack) throws JsonProcessingException {
        GroupChatMembersAddedEvent event = mapper.readValue(message, GroupChatMembersAddedEvent.class);

        chatService.addMembers(event);

        ack.acknowledge();
    }
}