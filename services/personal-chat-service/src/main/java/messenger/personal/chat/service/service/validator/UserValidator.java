package messenger.personal.chat.service.service.validator;

import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.client.grpc.UserGrpcClient;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.exception.AuthorizationException;
import messenger.personal.chat.service.exception.UserIsNotActive;
import messenger.personal.chat.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserValidator {

    private final UserGrpcClient userGrpcClient;

    public void validateUsersExist(List<Long> userIds) {
        User.UsersExistResponse response = userGrpcClient.validateUsersExist(userIds);

        response.getResultsList()
                .forEach(this::validateSingleResult);
    }

    private void validateSingleResult(User.UsersExistResponse.UserExistence result) {
        if (!result.getExists()) {
            throw new UserNotFoundException(
                    String.format("User %d not found", result.getUserId())
            );
        }

        if (!result.getIsActive()) {
            throw new UserIsNotActive(
                    String.format("User %d not active", result.getUserId())
            );
        }
    }

    public void validateUserHasRightsToTheChat(PersonalChat chat, Long userId) {
        if (!chat.getUser1Id().equals(userId) && !chat.getUser2Id().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d has no rights to the chat %d", userId, chat.getId())
            );
        }
    }
}
