package messenger.personal.chat.service.service.validator;

import messenger.personal.chat.service.client.grpc.UserGrpcClient;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.exception.AuthorizationException;
import messenger.personal.chat.service.exception.UserIsNotActive;
import messenger.personal.chat.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserGrpcClient userGrpcClient;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void shouldPassWhenAllUsersExistAndActive() {
        User.UsersExistResponse response =
                User.UsersExistResponse.newBuilder()
                        .addResults(
                                User.UsersExistResponse.UserExistence.newBuilder()
                                        .setUserId(1L)
                                        .setExists(true)
                                        .setIsActive(true)
                                        .build()
                        )
                        .build();

        when(userGrpcClient.validateUsersExist(List.of(1L)))
                .thenReturn(response);

        assertDoesNotThrow(() ->
                userValidator.validateUsersExist(List.of(1L))
        );
    }

    @Test
    void shouldThrowUserNotFoundException() {
        User.UsersExistResponse response =
                User.UsersExistResponse.newBuilder()
                        .addResults(
                                User.UsersExistResponse.UserExistence.newBuilder()
                                        .setUserId(2L)
                                        .setExists(false)
                                        .setIsActive(true)
                                        .build()
                        )
                        .build();

        when(userGrpcClient.validateUsersExist(List.of(2L)))
                .thenReturn(response);

        assertThrows(UserNotFoundException.class, () ->
                userValidator.validateUsersExist(List.of(2L))
        );
    }

    @Test
    void shouldThrowUserIsNotActiveException() {
        User.UsersExistResponse response =
                User.UsersExistResponse.newBuilder()
                        .addResults(
                                User.UsersExistResponse.UserExistence.newBuilder()
                                        .setUserId(3L)
                                        .setExists(true)
                                        .setIsActive(false)
                                        .build()
                        )
                        .build();

        when(userGrpcClient.validateUsersExist(List.of(3L)))
                .thenReturn(response);

        assertThrows(UserIsNotActive.class, () ->
                userValidator.validateUsersExist(List.of(3L))
        );
    }

    @Test
    void shouldPassWhenUserHasRights_user1() {
        PersonalChat chat = PersonalChat.builder()
                .id(1L)
                .user1Id(10L)
                .user2Id(20L)
                .build();

        assertDoesNotThrow(() ->
                userValidator.validateUserHasRightsToTheChat(chat, 10L)
        );
    }

    @Test
    void shouldPassWhenUserHasRights_user2() {
        PersonalChat chat = PersonalChat.builder()
                .id(1L)
                .user1Id(10L)
                .user2Id(20L)
                .build();

        assertDoesNotThrow(() ->
                userValidator.validateUserHasRightsToTheChat(chat, 20L)
        );
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUserHasNoRights() {
        PersonalChat chat = PersonalChat.builder()
                .id(1L)
                .user1Id(10L)
                .user2Id(20L)
                .build();

        assertThrows(AuthorizationException.class, () ->
                userValidator.validateUserHasRightsToTheChat(chat, 99L)
        );
    }
}
