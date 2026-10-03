package com.burkina.messenger.service.event;

import com.burkina.messenger.exception.EventSerializationException;
import com.burkina.messenger.mapper.SavedChatMapper;
import com.burkina.messenger.outbox.enums.EventType;
import com.burkina.messenger.outbox.service.OutboxEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.SavedChat;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedChatPublisher {

    private final ObjectMapper objectMapper;
    private final SavedChatMapper chatMapper;
    private final OutboxEventService outboxEventService;

    public void publishSavedChatCreated(SavedChat chat) {
        publish(
                EventType.SAVED_CHAT_CREATED,
                chatMapper.toSavedChatCreatedEvent(chat)
        );
    }

    public void publishSavedChatDeleted(SavedChat chat) {
        publish(
                EventType.SAVED_CHAT_DELETED,
                chatMapper.toSavedChatDeletedEvent(chat)
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
