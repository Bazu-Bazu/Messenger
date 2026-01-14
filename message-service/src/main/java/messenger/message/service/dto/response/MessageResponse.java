package messenger.message.service.dto.response;

import lombok.Builder;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.domain.enums.MessageType;

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
