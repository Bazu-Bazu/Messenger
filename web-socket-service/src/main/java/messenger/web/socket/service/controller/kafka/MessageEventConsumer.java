package messenger.web.socket.service.controller.kafka;

import dto.event.MessageDetailEvent;
import dto.response.MessageResponse;
import dto.result.MessageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "message_sending")
    public void handleMessageEvent(MessageDetailEvent event) {
        MessageResponse response = convertToMessageResponse(event);

        String destination = String.format("/topic/chat.%s.%s",
                event.chatType().toString().toLowerCase(),
                event.chatId());

        messagingTemplate.convertAndSend(destination, MessageResult.success(response));
    }

    private MessageResponse convertToMessageResponse(MessageDetailEvent event) {
        return MessageResponse.builder()
                .id(event.id())
                .chatId(event.chatId())
                .chatType(event.chatType())
                .content(event.content())
                .senderId(event.senderId())
                .messageType(event.messageType())
                .createdAt(event.createdAt())
                .editedAt(event.editedAt())
                .readAt(event.readAt())
                .build();
    }

}
