package messenger.message.service.kafka.producer;

import dto.event.MessageDetailEvent;
import dto.event.MessageShortEvent;
import lombok.RequiredArgsConstructor;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.mapper.MessageMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MessageEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MessageMapper messageMapper;

    public void publishMessageNotification(Message message, Set<Long> memberIds) {
        CompletableFuture.runAsync(() -> {
            publishToNotificationService(message, memberIds);
        });

        CompletableFuture.runAsync(() -> {
            switch (message.getChatType()) {
                case PERSONAL -> publishToPersonalChatService(message);
                case GROUP -> publishToGroupChatService(message);
            }
        });
    }

    private void publishToNotificationService(Message message, Set<Long> memberIds) {
        MessageDetailEvent event = messageMapper.toMessageDetailEvent(message, memberIds);

        kafkaTemplate.send("message_sending", event.id().toString(), event);
    }

    private void publishToGroupChatService(Message message) {
        MessageShortEvent event = messageMapper.toMessageShortEvent(message);

        kafkaTemplate.send("activity_group_chat", event.id().toString(), event);
    }

    private void publishToPersonalChatService(Message message) {
        MessageShortEvent event = messageMapper.toMessageShortEvent(message);

        kafkaTemplate.send("activity_personal_chat", event.id().toString(), event);
    }
}
