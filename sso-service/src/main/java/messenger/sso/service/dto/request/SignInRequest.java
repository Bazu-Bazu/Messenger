package messenger.sso.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        String phone,

        @NotBlank
        String password
) {}
