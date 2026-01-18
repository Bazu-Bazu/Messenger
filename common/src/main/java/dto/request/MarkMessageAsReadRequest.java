package dto.request;

import jakarta.validation.constraints.NotNull;
import enums.ChatType;

public record MarkMessageAsReadRequest(
        @NotNull
        Long messageId,

        @NotNull
        Long chatId,

        @NotNull
        ChatType chatType
) {}
