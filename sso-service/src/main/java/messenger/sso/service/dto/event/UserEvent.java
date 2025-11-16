package messenger.sso.service.dto.event;

import lombok.Builder;

@Builder
public record UserEvent(
        Long id,
        String userName,
        String phone,
        String email,
        String password
) {}
