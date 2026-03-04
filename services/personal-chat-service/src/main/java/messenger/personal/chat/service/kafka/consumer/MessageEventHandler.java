package messenger.personal.chat.service.kafka.consumer;

import dto.event.MessageShortEvent;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.service.PersonalChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventHandler {

    private final PersonalChatService personalChatService;

    @KafkaListener(topics = "activity_personal_chat")
    public void updatePersonalChatLastActivity(MessageShortEvent event) {
        personalChatService.updateLastActivity(event.chatId(), event.createdAt());
    }
}
