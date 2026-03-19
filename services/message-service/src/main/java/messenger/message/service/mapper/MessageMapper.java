package messenger.message.service.mapper;

import dto.event.MessageDetailEvent;
import dto.event.MessageShortEvent;
import dto.payload.MediaPayload;
import dto.payload.MessagePayload;
import dto.payload.TextPayload;
import dto.response.MessageResponse;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.exception.IllegalPayloadTypeException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessageMapper {

    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .payload(mapPayload(message))
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
                .payload(mapPayload(message))
                .messageType(message.getMessageType())
                .chatId(message.getChatId())
                .chatType(message.getChatType())
                .createdAt(message.getCreatedAt())
                .readAt(message.getReadAt())
                .memberIds(memberIds)
                .build();
    }

    private MessagePayload mapPayload(Message message) {
        if (message.getText() != null) {
            return new TextPayload(message.getText());
        }

        if (message.getMediaId() != null) {
            return new MediaPayload(message.getMediaId());
        }

        throw new IllegalPayloadTypeException("Message has no payload: " + message.getId());
    }
}
