package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import messenger.user.service.validation.annotation.UniqueUsername;

public record UpdateUsernameRequest(

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 5, max = 25, message = "Username must be 5-25 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers and underscores")
        @UniqueUsername
        String username
) {}
