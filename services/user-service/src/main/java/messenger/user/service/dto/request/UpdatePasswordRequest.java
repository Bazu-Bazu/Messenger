package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain uppercase letter")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain lowercase letter")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain digit")
        String password
) {}
