package messenger.personal.chat.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePersonalChatRequest(

        @NotNull(message = "User id cannot be null")
        Long userId
) {}
