package messenger.chat.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.PersonalChatEvent;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.service.PersonalChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalChatEventHandler {

    private final PersonalChatService chatService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "personal-chat-event")
    public void handlePersonalChatEvent(String message) throws JsonProcessingException {
        PersonalChatEvent event = mapper.readValue(message, PersonalChatEvent.class);

        switch (event.eventType()) {
            case PERSONAL_CHAT_CREATED -> chatService.create(event);
            case PERSONAL_CHAT_DELETED -> chatService.delete(event);
        }
    }
}
