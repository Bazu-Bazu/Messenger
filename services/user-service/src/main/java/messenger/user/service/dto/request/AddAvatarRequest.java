package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddAvatarRequest(
        @NotBlank
        String url
) {}
