package dto.event;

import enums.UserUpdateType;
import lombok.Builder;

@Builder
public record UserUpdatingEvent (
        Long id,
        String updatedField,
        UserUpdateType type
) {}
