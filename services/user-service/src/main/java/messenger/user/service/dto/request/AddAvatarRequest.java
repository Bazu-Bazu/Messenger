package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddAvatarRequest(

        @NotBlank(message = "URL cannot be empty")
        @Size(max = 255, message = "URL must not exceed 255 characters")
        @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http or https")
        String url
) {}
