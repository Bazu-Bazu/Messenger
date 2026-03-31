package messenger.sso.service.service;

import dto.event.UserEvent;
import enums.UserEventType;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.domain.repository.SsoUserRepository;
import messenger.sso.service.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SsoUserServiceTest {

    @Mock
    private SsoUserRepository ssoUserRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private SsoUserService ssoUserService;

    @Test
    void createSsoUser_shouldSaveUser() {
        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_REGISTERED,
                "username",
                "79999999999",
                "mail@test.com",
                "encodedPassword",
                10L
        );

        ssoUserService.createSsoUser(event);

        verify(ssoUserRepository).save(argThat(user ->
                user.getId().equals(1L) &&
                        user.getUsername().equals("username") &&
                        user.getPhone().equals("79999999999") &&
                        user.getEmail().equals("mail@test.com") &&
                        user.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    void updatePhone_shouldUpdatePhone_andDeleteTokens() {
        SsoUser user = SsoUser.builder()
                .id(1L)
                .phone("old")
                .build();

        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_PHONE_UPDATED,
                "username",
                "newPhone",
                "mail@test.com",
                "encodedPassword",
                10L
        );

        when(ssoUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ssoUserService.updatePhone(event);

        assertEquals("newPhone", user.getPhone());

        verify(refreshTokenService).deleteAllByUserId(1L);
    }

    @Test
    void updatePassword_shouldUpdatePassword_andDeleteTokens() {

        SsoUser user = SsoUser.builder()
                .id(1L)
                .password("oldPass")
                .build();

        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_PASSWORD_UPDATED,
                "username",
                "79999999999",
                "mail@test.com",
                "newPass",
                10L
        );

        when(ssoUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ssoUserService.updatePassword(event);

        assertEquals("newPass", user.getPassword());

        verify(refreshTokenService).deleteAllByUserId(1L);
    }

    @Test
    void updateEmail_shouldUpdateEmail_only() {
        SsoUser user = SsoUser.builder()
                .id(1L)
                .email("old@mail.com")
                .build();

        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_EMAIL_UPDATED,
                "username",
                "79999999999",
                "new@mail.com",
                "encodedPassword",
                10L
        );

        when(ssoUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ssoUserService.updateEmail(event);

        assertEquals("new@mail.com", user.getEmail());

        verifyNoInteractions(refreshTokenService);
    }

    @Test
    void updateUsername_shouldUpdateUsername_only() {
        SsoUser user = SsoUser.builder()
                .id(1L)
                .username("old")
                .build();

        UserEvent event = new UserEvent(
                1L,
                UserEventType.USER_USERNAME_UPDATED,
                "newName",
                "79999999999",
                "mail@test.com",
                "encodedPassword",
                10L
        );

        when(ssoUserRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ssoUserService.updateUsername(event);

        assertEquals("newName", user.getUsername());

        verifyNoInteractions(refreshTokenService);
    }

    @Test
    void findSsoUserById_shouldThrow_whenNotFound() {
        when(ssoUserRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(SsoUserNotFoundException.class,
                () -> ssoUserService.findSsoUserById(1L));
    }

    @Test
    void findSsoUserByPhone_shouldThrow_whenNotFound() {
        when(ssoUserRepository.findByPhone("799"))
                .thenReturn(Optional.empty());

        assertThrows(SsoUserNotFoundException.class,
                () -> ssoUserService.findSsoUserByPhone("799"));
    }
}
