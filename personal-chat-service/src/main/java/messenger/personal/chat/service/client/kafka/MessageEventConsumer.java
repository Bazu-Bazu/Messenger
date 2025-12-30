package messenger.personal.chat.service.client.kafka;

import dto.event.PersonalChatActivityEvent;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageEventConsumer {

    private final PersonalChatRepository personalChatRepository;

    @KafkaListener(topics = "personal-chat-events")
    public void updatePersonalChatLastActivity(PersonalChatActivityEvent event) {
        personalChatRepository.updateLastActivity(event.personalChatId(), event.lastActivityAt());
    }

}
