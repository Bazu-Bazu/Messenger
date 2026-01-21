package dto.request;

import jakarta.validation.constraints.NotNull;
import enums.ChatType;
import lombok.Builder;

@Builder
public record MarkMessageAsReadRequest(
        @NotNull
        Long messageId,

        @NotNull
        Long chatId,

        @NotNull
        ChatType chatType
) {}
