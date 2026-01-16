package dto.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageDetailEvent(
        Long id,
        Long chatId,
        String chatType,
        String content,
        Long senderId,
        String messageType,
        Instant createdAt
) {}
