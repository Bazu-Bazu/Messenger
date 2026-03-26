package messenger.sso.service.kafka.consumer;

import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.service.SsoUserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final SsoUserService ssoUserService;

    @KafkaListener(topics = "user-event")
    public void handleUserEvent(UserEvent event) {
        switch (event.eventType()) {
            case USER_REGISTERED -> ssoUserService.createSsoUser(event);
            case USER_PHONE_UPDATED -> ssoUserService.updatePhone(event);
            case USER_PASSWORD_UPDATED -> ssoUserService.updatePassword(event);
            case USER_EMAIL_UPDATED -> ssoUserService.updateEmail(event);
            case USER_USERNAME_UPDATED -> ssoUserService.updateUsername(event);
        }
    }
}
