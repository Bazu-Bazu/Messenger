package messenger.group.chat.service.validator;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.dto.UserExistenceDto;
import messenger.group.chat.service.exception.UserIsNotActive;
import messenger.group.chat.service.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserGrpcClient userGrpcClient;

    public void validateUsersExist(List<Long> userIds) {
        List<UserExistenceDto> responses = userGrpcClient.validateUsersExist(userIds);

        responses.forEach(this::validateSingleResult);
    }

    private void validateSingleResult(UserExistenceDto result) {
        if (!result.exists()) {
            throw new UserNotFoundException(
                    String.format("User %d not found", result.userId())
            );
        }

        if (!result.isActive()) {
            throw new UserIsNotActive(
                    String.format("User %d not active", result.userId())
            );
        }
    }
}
