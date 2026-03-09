package messenger.group.chat.service.kafka.consumer;

import dto.event.MessageShortEvent;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.service.GroupService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventHandler {

    private final GroupService groupChatService;

    @KafkaListener(topics = "activity_group_chat")
    public void updateGroupChatLastActivity(MessageShortEvent event) {
        groupChatService.updateLastActivity(event.chatId(), event.createdAt());
    }
}