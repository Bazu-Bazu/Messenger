package messenger.sso.service.kafka.consumer;

import dto.event.UserEvent;
import enums.UserEventType;
import messenger.sso.service.service.SsoUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserEventHandlerTest {

    @Mock
    private SsoUserService ssoUserService;

    @InjectMocks
    private UserEventHandler userEventHandler;

    @Test
    void handleUserEvent_shouldCallCreate_whenUserRegistered() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_REGISTERED,
                "john",
                "79999999999",
                "john@mail.com",
                "pass",
                10L
        );

        userEventHandler.handleUserEvent(event);

        verify(ssoUserService).createSsoUser(event);
    }

    @Test
    void handleUserEvent_shouldCallUpdatePhone() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_PHONE_UPDATED,
                "john",
                "newPhone",
                "john@mail.com",
                "pass",
                10L
        );

        userEventHandler.handleUserEvent(event);

        verify(ssoUserService).updatePhone(event);
    }

    @Test
    void handleUserEvent_shouldCallUpdatePassword() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_PASSWORD_UPDATED,
                "john",
                "79999999999",
                "john@mail.com",
                "newPass",
                10L
        );

        userEventHandler.handleUserEvent(event);

        verify(ssoUserService).updatePassword(event);
    }

    @Test
    void handleUserEvent_shouldCallUpdateEmail() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_EMAIL_UPDATED,
                "john",
                "79999999999",
                "new@mail.com",
                "pass",
                10L
        );

        userEventHandler.handleUserEvent(event);

        verify(ssoUserService).updateEmail(event);
    }

    @Test
    void handleUserEvent_shouldCallUpdateUsername() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_USERNAME_UPDATED,
                "newName",
                "79999999999",
                "john@mail.com",
                "pass",
                10L
        );

        userEventHandler.handleUserEvent(event);

        verify(ssoUserService).updateUsername(event);
    }
}
