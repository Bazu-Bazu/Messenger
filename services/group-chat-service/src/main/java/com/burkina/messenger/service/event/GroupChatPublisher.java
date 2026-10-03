package com.burkina.messenger.service.event;

import com.burkina.messenger.domain.entity.GroupChat;
import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.exception.EventSerializationException;
import com.burkina.messenger.mapper.GroupMapper;
import com.burkina.messenger.outbox.enums.EventType;
import com.burkina.messenger.outbox.service.OutboxEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class GroupChatPublisher {

    private final GroupMapper groupMapper;
    private final ObjectMapper objectMapper;
    private final OutboxEventService outboxEventService;

    public void publishGroupChatCreated(GroupChat chat) {
        publish(
                EventType.GROUP_CHAT_CREATED,
                groupMapper.toGroupChatCreatedEvent(chat)
        );
    }

    public void publishGroupChatDeleted(GroupChat chat) {
        publish(
                EventType.GROUP_CHAT_DELETED,
                groupMapper.toGroupChatDeletedEvent(chat)
        );
    }

    public void publishGroupChatInfoUpdated(GroupChat chat) {
        publish(
                EventType.GROUP_CHAT_INFO_UPDATED,
                groupMapper.toGroupChatInfoUpdatedEvent(chat)
        );
    }

    public void publishGroupChatMembersAdded(Long groupId, List<GroupChatMember> members) {
        publish(
                EventType.GROUP_CHAT_MEMBERS_ADDED,
                groupMapper.toGroupChatMembersAddedEvent(groupId, members)
        );
    }

    public void publishGroupChatMembersUpdated(Long groupId, List<GroupChatMember> members) {
        publish(
                EventType.GROUP_CHAT_MEMBERS_UPDATED,
                groupMapper.toGroupChatMembersUpdatedEvent(groupId, members)
        );
    }

    public void publishGroupChatMembersDeleted(Long groupId, List<Long> members) {
        publish(
                EventType.GROUP_CHAT_MEMBERS_DELETED,
                groupMapper.toGroupChatMembersDeletedEvent(groupId, members)
        );
    }

    private void publish(EventType type, Object event) {
        try {
            outboxEventService.saveEvent(
                    type,
                    objectMapper.writeValueAsString(event)
            );
        } catch (JsonProcessingException e) {
            throw new EventSerializationException(
                    String.format("Failed to serialize event. Exception: %s", e)
            );
        }
    }
}
