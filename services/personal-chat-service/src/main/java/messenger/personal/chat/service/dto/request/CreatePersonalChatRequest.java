package messenger.personal.chat.service.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreatePersonalChatRequest(
        @NotNull
        Long userId
) {}
