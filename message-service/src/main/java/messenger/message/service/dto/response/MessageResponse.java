package messenger.message.service.dto.response;

import lombok.Builder;
import messenger.message.service.entity.MessageType;

import java.time.Instant;

@Builder
public record MessageResponse (
         Long id,
         Long chatId,
         String content,
         Long senderId,
         MessageType type,
         Instant createdAt,
         Instant editedAt,
         Instant readAt
) {}
