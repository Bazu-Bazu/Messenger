package com.burkina.messenger.validator;

import com.burkina.messenger.domain.entity.PersonalChat;
import com.burkina.messenger.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRightsValidator {

    public void validateUserHasRightsToTheChat(PersonalChat chat, Long userId) {
        if (!chat.getUser1Id().equals(userId) && !chat.getUser2Id().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d has no rights to the chat %d", userId, chat.getId())
            );
        }
    }
}
