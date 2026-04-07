package messenger.personal.chat.service.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.SavedChatEvent;
import enums.ChatType;
import enums.SavedChatEventType;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.entity.SavedChat;
import messenger.personal.chat.service.service.outbox.OutboxEventService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedChatPublisher {

    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    private final static String KAFKA_TOPIC = "saved-chat-event";
    private final static String AGGREGATE_TYPE = ChatType.SAVED.name();

    public void publishChatCreated(SavedChat chat) {
        save(chat, SavedChatEventType.SAVED_CHAT_CREATED);
    }

    public void publishChatDeleted(SavedChat chat) {
        save(chat, SavedChatEventType.SAVED_CHAT_DELETED);
    }

    private void save(SavedChat chat, SavedChatEventType eventType) {
        try {
            SavedChatEvent event = SavedChatEvent.builder()
                    .id(chat.getId())
                    .eventType(eventType)
                    .userId(chat.getUserId())
                    .build();

            String payload = objectMapper.writeValueAsString(event);

            outboxEventService.saveEvent(KAFKA_TOPIC, eventType.name(), AGGREGATE_TYPE, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
