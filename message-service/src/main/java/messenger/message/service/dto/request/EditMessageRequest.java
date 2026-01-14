package messenger.message.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.domain.enums.MessageType;

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
        MessageType type
) {}
