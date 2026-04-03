package messenger.user.service.service.event;

import enums.UserEventType;
import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.entity.User;
import messenger.user.service.service.outbox.OutboxEventService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final OutboxEventService outboxEventService;

    public void publishUserRegistration(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_REGISTERED, user);
    }

    public void publishUserPhoneChanged(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_PHONE_UPDATED, user);
    }

    public void publishUserPasswordChanged(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_PASSWORD_UPDATED, user);
    }

    public void publishUserEmailChanged(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_EMAIL_UPDATED, user);
    }

    public void publishUserUsernameChanged(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_USERNAME_UPDATED, user);
    }

    public void publishUserAvatarChanged(User user) {
        outboxEventService.saveEvent("user-event", UserEventType.USER_AVATAR_UPDATED, user);
    }
}
