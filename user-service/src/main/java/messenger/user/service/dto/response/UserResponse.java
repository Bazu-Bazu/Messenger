package messenger.user.service.dto.response;

import lombok.Builder;
import messenger.user.service.domain.enums.UserStatus;

import java.time.Instant;

@Builder
public record UserResponse(
        Long id,
        String username,
        String phone,
        String email,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt,
        UserStatus status
) {}
