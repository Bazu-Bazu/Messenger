package com.burkina.messenger.mapper;

import com.burkina.common.dto.event.messenger.message.*;
import com.burkina.messenger.dto.request.MarkMessageAsReadRequest;
import com.burkina.messenger.dto.request.RemoveMessageRequest;
import com.burkina.messenger.dto.request.SendMessageRequest;
import com.burkina.messenger.dto.response.MessageReadResponse;
import com.burkina.messenger.dto.response.MessageRemovedResponse;
import com.burkina.messenger.dto.response.MessageSentResponse;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public SendMessageEvent toSendMessageEvent(SendMessageRequest request, Long userId) {
        return SendMessageEvent.builder()
                .text(request.getText())
                .messageType(request.getMessageType())
                .mediaId(request.getMediaId())
                .chatId(request.getChatId())
                .chatType(request.getChatType())
                .userId(userId)
                .build();
    }

    public MarkMessageAsReadEvent toMarkMessageAsReadEvent(MarkMessageAsReadRequest request, Long userId) {
        return MarkMessageAsReadEvent.builder()
                    .messageId(request.messageId())
                    .userId(userId)
                    .build();
    }

    public RemoveMessageEvent toRemoveMessageEvent(RemoveMessageRequest request, Long userId) {
        return RemoveMessageEvent.builder()
                .messageId(request.messageId())
                .userId(userId)
                .build();
    }

    public MessageSentResponse toMessageSentResponse(MessageSentEvent event) {
        return MessageSentResponse.builder()
                    .messageId(event.messageId())
                    .text(event.text())
                    .messageType(event.messageType())
                    .createdAt(event.createdAt())
                    .mediaId(event.mediaId())
                    .chatId(event.chatId())
                    .chatType(event.chatType())
                    .userId(event.userId())
                    .build();
    }

    public MessageRemovedResponse toMessageRemovedResponse(MessageRemovedEvent event) {
        return MessageRemovedResponse.builder()
                .messageId(event.messageId())
                .chatId(event.chatId())
                .chatType(event.chatType())
                .build();
    }

    public MessageReadResponse toMessageReadResponse(MessageReadEvent event) {
        return MessageReadResponse.builder()
                .messageId(event.messageId())
                .chatId(event.chatId())
                .chatType(event.chatType())
                .build();
    }
}
