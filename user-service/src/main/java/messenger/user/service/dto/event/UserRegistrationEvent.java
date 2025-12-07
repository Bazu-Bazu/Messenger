package messenger.user.service.dto.event;

import lombok.Builder;

@Builder
public record UserRegistrationEvent(
        Long id,
        String userName,
        String phone,
        String email,
        String password
) {}


