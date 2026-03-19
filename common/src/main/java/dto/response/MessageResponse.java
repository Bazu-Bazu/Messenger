package dto.response;

import dto.payload.MessagePayload;
import enums.ChatType;
import enums.MessageType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResponse (

         Long id,
         Long chatId,
         ChatType chatType,
         MessagePayload payload,
         Long senderId,
         MessageType messageType,
         Instant createdAt,
         Instant readAt
) {}
