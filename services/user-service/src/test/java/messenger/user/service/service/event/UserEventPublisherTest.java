package messenger.user.service.service.event;

import enums.UserEventType;
import messenger.user.service.domain.entity.User;
import messenger.user.service.service.outbox.OutboxEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserEventPublisherTest {

    @Mock
    private OutboxEventService outboxEventService;

    @InjectMocks
    private UserEventPublisher userEventPublisher;

    @Test
    void publishUserRegistration_shouldCallOutboxEventService() {
        User user = User.builder()
                .id(1L).username("john")
                .build();

        userEventPublisher.publishUserRegistration(user);

        verify(outboxEventService).saveEvent("user-event", UserEventType.USER_REGISTERED, user);
    }

    @Test
    void publishUserPhoneChanged_shouldCallOutboxEventService() {
        User user = User.builder().id(1L).username("john").build();

        userEventPublisher.publishUserPhoneChanged(user);

        verify(outboxEventService).saveEvent("user-event", UserEventType.USER_PHONE_UPDATED, user);
    }

    @Test
    void publishUserPasswordChanged_shouldCallOutboxEventService() {
        User user = User.builder().id(1L).username("john").build();

        userEventPublisher.publishUserPasswordChanged(user);

        verify(outboxEventService).saveEvent("user-event", UserEventType.USER_PASSWORD_UPDATED, user);
    }

    @Test
    void publishUserEmailChanged_shouldCallOutboxEventService() {
        User user = User.builder().id(1L).username("john").build();

        userEventPublisher.publishUserEmailChanged(user);

        verify(outboxEventService).saveEvent("user-event", UserEventType.USER_EMAIL_UPDATED, user);
    }

    @Test
    void publishUserUsernameChanged_shouldCallOutboxEventService() {
        User user = User.builder().id(1L).username("john").build();

        userEventPublisher.publishUserUsernameChanged(user);

        verify(outboxEventService).saveEvent("user-event", UserEventType.USER_USERNAME_UPDATED, user);
    }
}
