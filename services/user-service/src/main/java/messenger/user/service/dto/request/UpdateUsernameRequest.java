package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameRequest(
        @NotBlank
        String username
) {}
