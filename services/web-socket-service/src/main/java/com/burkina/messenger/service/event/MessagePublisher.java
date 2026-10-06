package com.burkina.messenger.service.event;

import com.burkina.common.dto.event.messenger.message.MarkMessageAsReadEvent;
import com.burkina.common.dto.event.messenger.message.RemoveMessageEvent;
import com.burkina.common.dto.event.messenger.message.SendMessageEvent;
import com.burkina.messenger.exception.EventSerializationException;
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
    private final OutboxEventService outboxEventService;

    public void publishSendMessageEvent(SendMessageEvent event) {
        publish(
                EventType.SEND_MESSAGE_EVENT,
                event
        );
    }

    public void publishMarkMessageAsReadEvent(MarkMessageAsReadEvent event) {
        publish(
                EventType.MARK_MESSAGE_AS_READ_EVENT,
                event
        );
    }

    public void publishRemoveMessageEvent(RemoveMessageEvent event) {
        publish(
                EventType.REMOVE_MESSAGE_EVENT,
                event
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
