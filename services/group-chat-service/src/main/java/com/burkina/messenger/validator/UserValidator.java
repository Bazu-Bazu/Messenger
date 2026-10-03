package com.burkina.messenger.validator;

import com.burkina.common.dto.response.UserStatusResponse;
import com.burkina.messenger.client.IdentityUserClient;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.exception.UserIsNotActive;
import com.burkina.messenger.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final IdentityUserClient userClient;

    public void validateUsersExist(List<Long> userIds) {
        List<UserStatusResponse> responses = userClient.getUsersStatus(userIds);

        responses.forEach(this::validateSingleResult);
    }

    private void validateSingleResult(UserStatusResponse response) {
        if (!response.exists()) {
            throw new UserNotFoundException(
                    String.format("User %d not found", response.userId())
            );
        }

        if (!response.active()) {
            throw new UserIsNotActive(
                    String.format("User %d not active", response.userId())
            );
        }
    }
}
