package messenger.message.service.controller.kafka;

import dto.event.MessageDetailEvent;
import dto.event.MessageShortEvent;
import lombok.RequiredArgsConstructor;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.domain.enums.ChatType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MessageEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMessageNotification(Message message, Set<Long> memberIds) {
        CompletableFuture.runAsync(() -> {
            publishToNotificationService(message, memberIds);
        });

        CompletableFuture.runAsync(() -> {
            if (message.getChatType().equals(ChatType.PERSONAL)) {
                publishToPersonalChatService(message);
            } else {
                publishToGroupChatService(message);
            }
        });
    }

    private void publishToNotificationService(Message message, Set<Long> memberIds) {
        MessageDetailEvent event = createMessageDetailEvent(message);

        kafkaTemplate.send("message_sending", event.id().toString(), event);
    }

    private MessageDetailEvent createMessageDetailEvent(Message message) {
        return MessageDetailEvent.builder()
                .id(message.getId())
                .content(message.getContent())
                .messageType(message.getMessageType().name())
                .chatId(message.getChatId())
                .chatType(message.getChatType().name())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private void publishToGroupChatService(Message message) {
        MessageShortEvent event = createMessageShortEvent(message);

        kafkaTemplate.send("activity_group_chat", event.id().toString(), event);
    }

    private void publishToPersonalChatService(Message message) {
        MessageShortEvent event = createMessageShortEvent(message);

        kafkaTemplate.send("activity_personal_chat", event.id().toString(), event);
    }

    private MessageShortEvent createMessageShortEvent(Message message) {
        return MessageShortEvent.builder()
                .id(message.getId())
                .chatId(message.getChatId())
                .chatType(message.getChatType().name())
                .createdAt(message.getCreatedAt())
                .build();
    }

}
