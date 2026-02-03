package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(
        @NotBlank
        String password
) {}
