package dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import enums.ChatType;
import enums.MessageType;
import lombok.Builder;

@Builder
public record EditMessageRequest (
        @NotNull
        Long messageId,

        @NotNull
        Long chatId,

        @NotNull
        ChatType chatType,

        @NotBlank
        String content,

        @NotNull
        MessageType messageType
) {}
