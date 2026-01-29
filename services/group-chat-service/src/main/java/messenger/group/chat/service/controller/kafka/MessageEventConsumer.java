package messenger.group.chat.service.controller.kafka;

import dto.event.MessageShortEvent;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.service.GroupChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventConsumer {

    private final GroupChatService groupChatService;

    @KafkaListener(topics = "activity_group_chat")
    public void updateGroupChatLastActivity(MessageShortEvent event) {
        groupChatService.updateLastActivity(event.chatId(), event.createdAt());
    }

}