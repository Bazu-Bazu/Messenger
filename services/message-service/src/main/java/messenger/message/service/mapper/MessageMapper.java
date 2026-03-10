package messenger.message.service.mapper;

import dto.event.MessageDetailEvent;
import dto.event.MessageShortEvent;
import dto.response.MessageResponse;
import messenger.message.service.domain.entity.Message;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .chatId(message.getChatId())
                .createdAt(message.getCreatedAt())
                .readAt(message.getReadAt())
                .messageType(message.getMessageType())
                .chatType(message.getChatType())
                .build();
    }

    public MessageShortEvent toMessageShortEvent(Message message) {
        return MessageShortEvent.builder()
                .id(message.getId())
                .chatId(message.getChatId())
                .chatType(message.getChatType())
                .createdAt(message.getCreatedAt())
                .build();
    }

    public MessageDetailEvent toMessageDetailEvent(Message message, Set<Long> memberIds) {
        return MessageDetailEvent.builder()
                .id(message.getId())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .chatId(message.getChatId())
                .chatType(message.getChatType())
                .createdAt(message.getCreatedAt())
                .readAt(message.getReadAt())
                .memberIds(memberIds)
                .build();
    }
}
