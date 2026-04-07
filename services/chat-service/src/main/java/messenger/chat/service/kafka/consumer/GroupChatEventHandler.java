package messenger.chat.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.GroupChatEvent;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.service.GroupChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupChatEventHandler {

    private final GroupChatService chatService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "group-chat-event")
    public void handleGroupChatEvent(String message) throws JsonProcessingException {
        GroupChatEvent event = mapper.readValue(message, GroupChatEvent.class);

        switch (event.eventType()) {
            case GROUP_CHAT_CREATED -> chatService.create(event);
            case GROUP_CHAT_ADDED_MEMBERS -> chatService.addMembers(event);
            case GROUP_CHAT_CHANGED_ROLES -> chatService.changePermissions(event);
            case GROUP_CHAT_REMOVED_MEMBERS -> chatService.removeMembers(event);
            case GROUP_CHAT_CHANGED_INFO -> chatService.changeInfo(event);
            case GROUP_CHAT_DELETED -> chatService.delete(event);
        }
    }
}