package com.burkina.messenger.mapper;

import com.burkina.common.dto.event.messenger.message.MessageReadEvent;
import com.burkina.common.dto.event.messenger.message.MessageRemovedEvent;
import com.burkina.common.dto.event.messenger.message.MessageSentEvent;
import com.burkina.messenger.domain.entity.Message;
import com.burkina.messenger.dto.response.MessageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageMapper {

    public List<MessageResponse> toMessageResponseList(List<Message> messages) {
        return messages.stream()
                        .map(this::toMessageResponse)
                        .toList();
    }

    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getId())
                .text(message.getText())
                .mediaId(message.getMediaId())
                .userId(message.getSenderId())
                .chatId(message.getChatId())
                .createdAt(message.getCreatedAt())
                .read(message.getRead())
                .messageType(message.getMessageType())
                .chatType(message.getChatType())
                .build();
    }

    public MessageSentEvent toMessageSentEvent(Message message) {
        return MessageSentEvent.builder()
                    .messageId(message.getId())
                    .chatId(message.getChatId())
                    .chatType(message.getChatType())
                    .text(message.getText())
                    .messageType(message.getMessageType())
                    .createdAt(message.getCreatedAt())
                    .mediaId(message.getMediaId())
                    .userId(message.getSenderId())
                    .build();

    }

    public MessageRemovedEvent toMessageRemovedEvent(Message message) {
        return MessageRemovedEvent.builder()
                    .messageId(message.getId())
                    .build();
    }

    public MessageReadEvent toMessageReadEvent(Message message) {
        return MessageReadEvent.builder()
                    .messageId(message.getId())
                    .build();
    }
}
