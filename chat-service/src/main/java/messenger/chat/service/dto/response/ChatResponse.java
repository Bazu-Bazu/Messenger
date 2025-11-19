package messenger.chat.service.dto.response;

import lombok.Builder;
import messenger.chat.service.entity.ChatType;

import java.time.Instant;
import java.util.List;

@Builder
public record ChatResponse(
        Long id,
        ChatType type,
        String name,
        String description,
        String avatarUrl,
        Long createdBy,
        Instant createdAt,
        Instant updatedAt,
        List<Long> membersIds
) {}
