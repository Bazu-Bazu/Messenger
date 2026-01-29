package messenger.sso.service.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {}
