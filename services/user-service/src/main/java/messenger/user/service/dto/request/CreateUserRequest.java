package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import messenger.user.service.validation.annotation.UniquePhone;
import messenger.user.service.validation.annotation.UniqueUsername;

public record CreateUserRequest(

        @NotBlank(message = "Username cannot be empty")
        @Size(min = 5, max = 25, message = "Username must be 5-25 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers and underscores")
        @UniqueUsername
        String username,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
        @UniquePhone
        String phone,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain uppercase letter")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain lowercase letter")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain digit")
        String password
) {}
