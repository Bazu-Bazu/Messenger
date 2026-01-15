package messenger.message.service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import messenger.message.service.domain.enums.ChatType;

public record GetMessagesRequest(
        @NotNull
        Long chatId,

        @NotNull
        ChatType chatType,

        @Min(0)
        int page
) {}
