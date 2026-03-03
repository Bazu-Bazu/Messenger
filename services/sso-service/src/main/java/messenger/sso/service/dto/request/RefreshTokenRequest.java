package messenger.sso.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "Token cannot be empty")
        String refreshToken
) {}
