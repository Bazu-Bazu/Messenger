package com.burkina.messenger.service.event;

import com.burkina.messenger.domain.entity.Message;
import com.burkina.messenger.exception.EventSerializationException;
import com.burkina.messenger.mapper.MessageMapper;
import com.burkina.messenger.outbox.enums.EventType;
import com.burkina.messenger.outbox.service.OutboxEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessagePublisher {

    private final ObjectMapper objectMapper;
    private final MessageMapper messageMapper;
    private final OutboxEventService outboxEventService;

    public void publishMessageSentEvent(Message message) {
        publish(
                EventType.MESSAGES_SENT_EVENT,
                messageMapper.toMessageSentEvent(message)
        );
    }

    public void publishMessageRemovedEvent(Message message) {
        publish(
                EventType.MESSAGE_REMOVED_EVENT,
                messageMapper.toMessageRemovedEvent(message)
        );
    }

    public void publishMessageReadEvent(Message message) {
        publish(
                EventType.MESSAGE_READ_EVENT,
                messageMapper.toMessageReadEvent(message)
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
