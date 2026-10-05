package com.burkina.messenger.kafka.consumer;


import com.burkina.common.dto.event.messenger.message.MarkMessageAsReadEvent;
import com.burkina.common.dto.event.messenger.message.RemoveMessageEvent;
import com.burkina.common.dto.event.messenger.message.SendMessageEvent;
import com.burkina.messenger.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketConsumer {

    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    @KafkaListener(topics = "send-message")
    public void handleMessage(String message, Acknowledgment ack) throws JsonProcessingException {
        SendMessageEvent event = objectMapper.readValue(message, SendMessageEvent.class);
        messageService.sendMessage(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "mark-message-as-read")
    public void handleMarkMessageAsRead(String message, Acknowledgment ack) throws JsonProcessingException {
        MarkMessageAsReadEvent event = objectMapper.readValue(message, MarkMessageAsReadEvent.class);
        messageService.markMessageAsRead(event);

        ack.acknowledge();
    }

    @KafkaListener(topics = "remove-message")
    public void handleRemoveMessage(String message, Acknowledgment ack) throws JsonProcessingException {
        RemoveMessageEvent event = objectMapper.readValue(message, RemoveMessageEvent.class);
        messageService.removeMessage(event);

        ack.acknowledge();
    }
}
