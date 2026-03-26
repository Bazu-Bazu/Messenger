package messenger.user.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddAvatarRequest(

        @NotNull(message = "Id cannot be empty")
        Long id
) {}
