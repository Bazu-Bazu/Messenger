package messenger.sso.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Phone cannot be empty")
        String phone,

        @NotBlank(message = "Password cannot be empty")
        String password
) {}
