package messenger.personal.chat.service.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.PersonalChatEvent;
import enums.ChatType;
import enums.PersonalChatEventType;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.service.outbox.OutboxEventService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalChatPublisher {

    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    private final static String KAFKA_TOPIC = "personal-chat-event";
    private final static String AGGREGATE_TYPE = ChatType.PERSONAL.name();

    public void publishChatCreated(PersonalChat chat) {
        save(chat, PersonalChatEventType.PERSONAL_CHAT_CREATED);
    }

    public void publishChatDeleted(PersonalChat chat) {
        save(chat, PersonalChatEventType.PERSONAL_CHAT_DELETED);
    }

    private void save(PersonalChat chat, PersonalChatEventType eventType) {
        try {
            PersonalChatEvent event = PersonalChatEvent.builder()
                    .id(chat.getId())
                    .eventType(eventType)
                    .user1Id(chat.getUser1Id())
                    .user2Id(chat.getUser2Id())
                    .build();

            String payload = objectMapper.writeValueAsString(event);

            outboxEventService.saveEvent(KAFKA_TOPIC, eventType.name(), AGGREGATE_TYPE, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
