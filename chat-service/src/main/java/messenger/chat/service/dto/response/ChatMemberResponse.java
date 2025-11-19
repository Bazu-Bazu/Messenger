package messenger.chat.service.dto.response;

import lombok.Builder;
import messenger.chat.service.entity.ChatMemberRole;

import java.time.Instant;

@Builder
public record ChatMemberResponse (
        Long id,
        Long chatId,
        Long userId,
        ChatMemberRole role,
        Instant joinedAt,
        Instant lastReadAt,
        String username
) {}
