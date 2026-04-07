package messenger.chat.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.SavedChatEvent;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.service.SavedChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavedChatEventHandler {

    private final SavedChatService chatService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "saved-chat-event")
    public void handleSavedChatEvent(String message) throws JsonProcessingException {
        SavedChatEvent event = mapper.readValue(message, SavedChatEvent.class);

        switch (event.eventType()) {
            case SAVED_CHAT_CREATED -> chatService.create(event);
            case SAVED_CHAT_DELETED -> chatService.delete(event);
        }
    }
}
