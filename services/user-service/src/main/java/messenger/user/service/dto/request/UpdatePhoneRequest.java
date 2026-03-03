package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import messenger.user.service.validation.annotation.UniquePhone;

public record UpdatePhoneRequest(

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
        @UniquePhone
        String phone
) {}
