package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank
        String username,

        @NotBlank
        String phone,

        @NotBlank
        String password
) {}
