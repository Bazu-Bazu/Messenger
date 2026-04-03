package dto.event;

import enums.UserEventType;
import lombok.Builder;

@Builder
public record UserEvent(

        Long id,
        UserEventType eventType,
        String username,
        String phone,
        String email,
        String password,
        Long avatarId
) {}
