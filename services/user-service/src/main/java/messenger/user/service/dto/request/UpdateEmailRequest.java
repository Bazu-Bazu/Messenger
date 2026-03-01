package messenger.user.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import messenger.user.service.validation.annotation.UniqueEmail;

public record UpdateEmailRequest(

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email is too long")
        @UniqueEmail
        String email
) {}
