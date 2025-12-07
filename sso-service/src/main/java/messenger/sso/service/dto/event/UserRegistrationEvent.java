package messenger.sso.service.dto.event;

import lombok.Builder;

@Builder
public record UserRegistrationEvent(
        Long id,
        String username,
        String phone,
        String email,
        String password
) {}
