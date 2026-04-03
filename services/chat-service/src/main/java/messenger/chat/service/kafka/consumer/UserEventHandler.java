package messenger.chat.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final UserService userService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "user-event")
    public void handleUserEvent(String message) throws JsonProcessingException {
        UserEvent event = mapper.readValue(message, UserEvent.class);

        switch (event.eventType()) {
            case USER_REGISTERED -> userService.createUser(event);
            case USER_AVATAR_UPDATED -> userService.updateAvatar(event);
        }
    }
}
