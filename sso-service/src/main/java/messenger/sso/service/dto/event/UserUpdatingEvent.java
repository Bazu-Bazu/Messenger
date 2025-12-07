package messenger.sso.service.dto.event;

import messenger.sso.service.service.UserUpdateType;

public record UserUpdatingEvent(
        Long id,
        String updatedField,
        UserUpdateType type
) {}
