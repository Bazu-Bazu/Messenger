package messenger.group.chat.service.validator;

import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.dto.UserExistenceDto;
import messenger.group.chat.service.exception.UserIsNotActive;
import messenger.group.chat.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserGrpcClient userGrpcClient;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void shouldPassWhenAllUsersExistAndActive() {
        List<Long> userIds = List.of(1L, 2L);

        List<UserExistenceDto> responses = List.of(
                new UserExistenceDto(1L, true, true),
                new UserExistenceDto(2L, true, true)
        );

        when(userGrpcClient.validateUsersExist(userIds)).thenReturn(responses);

        assertDoesNotThrow(() -> userValidator.validateUsersExist(userIds));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        List<Long> userIds = List.of(1L);

        List<UserExistenceDto> responses = List.of(
                new UserExistenceDto(1L, false, false)
        );

        when(userGrpcClient.validateUsersExist(userIds)).thenReturn(responses);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userValidator.validateUsersExist(userIds)
        );

        assertEquals("User 1 not found", exception.getMessage());
    }

    @Test
    void shouldThrowUserIsNotActiveWhenUserInactive() {
        List<Long> userIds = List.of(1L);

        List<UserExistenceDto> responses = List.of(
                new UserExistenceDto(1L, true, false)
        );

        when(userGrpcClient.validateUsersExist(userIds)).thenReturn(responses);

        UserIsNotActive exception = assertThrows(
                UserIsNotActive.class,
                () -> userValidator.validateUsersExist(userIds)
        );

        assertEquals("User 1 not active", exception.getMessage());
    }
}