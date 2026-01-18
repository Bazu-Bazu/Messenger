package dto.response;

import enums.ChatType;
import enums.MessageType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageResponse (
         Long id,
         Long chatId,
         ChatType chatType,
         String content,
         Long senderId,
         MessageType messageType,
         Instant createdAt,
         Instant editedAt,
         Instant readAt
) {}
