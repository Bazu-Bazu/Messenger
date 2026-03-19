package dto.request;

import dto.payload.MessagePayload;
import enums.ChatType;
import enums.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SendMessageRequest (

        @NotNull
        Long chatId,

        @NotNull
        MessagePayload payload,

        @NotNull
        ChatType chatType,

        @NotNull
        MessageType messageType
) {}
