package messenger.sso.service.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.event.UserEvent;
import messenger.sso.service.service.SsoUserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventHandler {

    private final SsoUserService ssoUserService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_registration")
    public void addUser(String message) throws JsonProcessingException {
        UserEvent event = objectMapper.readValue(message, UserEvent.class);

        ssoUserService.createSsoUser(event);
    }

}
