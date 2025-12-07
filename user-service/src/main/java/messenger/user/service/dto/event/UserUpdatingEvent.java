package messenger.user.service.dto.event;

import lombok.Builder;
import messenger.user.service.validation.UserUpdateType;

@Builder
public record UserUpdatingEvent (
        Long id,
        String updatedField,
        UserUpdateType type
) {}
