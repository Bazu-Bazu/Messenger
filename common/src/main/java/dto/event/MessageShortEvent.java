package dto.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageShortEvent(
        Long id,
        Long chatId,
        String chatType,
        Instant createdAt
) {}
