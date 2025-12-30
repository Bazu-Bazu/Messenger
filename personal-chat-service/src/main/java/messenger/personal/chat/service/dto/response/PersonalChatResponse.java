package messenger.personal.chat.service.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PersonalChatResponse(
        Long id,
        Long user1Id,
        Long user2Id,
        Instant createdAt,
        Instant lastActivityAt
) {}
