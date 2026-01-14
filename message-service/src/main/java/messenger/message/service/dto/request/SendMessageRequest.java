package messenger.message.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.domain.enums.MessageType;

public record SendMessageRequest (
        @NotNull
        Long chatId,

        @NotBlank
        String content,

        @NotNull
        ChatType chatType,

        @NotNull
        MessageType messageType
) {}
