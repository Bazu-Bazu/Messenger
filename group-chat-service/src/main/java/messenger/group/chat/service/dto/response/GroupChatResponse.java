package messenger.group.chat.service.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record GroupChatResponse(
        Long id,
        String name,
        String avatarUrl,
        String description,
        Long createdBy,
        Instant createdAt,
        Instant lastActivityAt
) {}
