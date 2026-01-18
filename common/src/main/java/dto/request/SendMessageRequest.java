package dto.request;

import enums.ChatType;
import enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
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
