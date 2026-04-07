package messenger.personal.chat.service.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SavedChatResponse(

        Long id,
        Long userId,
        Instant createdAt
) {}
