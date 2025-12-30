package dto.event;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PersonalChatActivityEvent(
        Long personalChatId,
        Instant lastActivityAt
) {}
