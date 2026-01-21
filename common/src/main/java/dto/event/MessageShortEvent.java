package dto.event;

import enums.ChatType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MessageShortEvent(
        Long id,
        Long chatId,
        ChatType chatType,
        Instant createdAt
) {}
