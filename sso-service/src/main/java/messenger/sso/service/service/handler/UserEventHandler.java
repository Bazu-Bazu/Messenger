package messenger.sso.service.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.event.UserRegistrationEvent;
import messenger.sso.service.dto.event.UserUpdatingEvent;
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
        UserRegistrationEvent event = objectMapper.readValue(message, UserRegistrationEvent.class);

        ssoUserService.createSsoUser(event);
    }

    @KafkaListener(topics = "user_updating")
    public void updateUser(String message) throws JsonProcessingException {
        UserUpdatingEvent event = objectMapper.readValue(message, UserUpdatingEvent.class);

        ssoUserService.updateSsoUser(event);
    }

}
