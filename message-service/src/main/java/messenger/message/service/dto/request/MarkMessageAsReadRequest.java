package messenger.message.service.dto.request;

import jakarta.validation.constraints.NotNull;
import messenger.message.service.domain.enums.ChatType;

public record MarkMessageAsReadRequest(
        @NotNull
        Long messageId,

        @NotNull
        Long chatId,

        @NotNull
        ChatType chatType
) {}
