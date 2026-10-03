package com.burkina.messenger.validator;

import com.burkina.common.dto.response.UserStatusResponse;
import com.burkina.messenger.client.IdentityUserClient;
import com.burkina.messenger.exception.UserIsNotActiveException;
import com.burkina.messenger.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final IdentityUserClient userClient;

    public void validateUser(Long userId) {
        UserStatusResponse response = userClient.getUserStatus(userId);

        if (!response.exists()) {
            throw new UserNotFoundException(
                    String.format("User with id %d not found", userId)
            );
        }

        if (!response.active()) {
            throw new UserIsNotActiveException(
                    String.format("User with id %d is not active", userId)
            );
        }
    }
}
