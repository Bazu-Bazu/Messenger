package com.burkina.messenger.service.event;

import com.burkina.messenger.domain.entity.PersonalChat;
import com.burkina.messenger.exception.EventSerializationException;
import com.burkina.messenger.mapper.PersonalChatMapper;
import com.burkina.messenger.outbox.enums.EventType;
import com.burkina.messenger.outbox.service.OutboxEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PersonalChatPublisher {

    private final ObjectMapper objectMapper;
    private final PersonalChatMapper chatMapper;
    private final OutboxEventService outboxEventService;

    public void publishPersonalChatCreated(PersonalChat chat) {
        publish(
                EventType.PERSONAL_CHAT_CREATED,
                chatMapper.toPersonalChatCreatedEvent(chat)
        );
    }

    public void publishPersonalChatDeleted(PersonalChat chat) {
        publish(
                EventType.PERSONAL_CHAT_DELETED,
                chatMapper.toPersonalChatDeletedEvent(chat)
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
