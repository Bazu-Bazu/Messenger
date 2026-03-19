package dto.event;

import dto.payload.MessagePayload;
import enums.ChatType;
import enums.MessageType;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record MessageDetailEvent(

        Long id,
        Long chatId,
        ChatType chatType,
        MessagePayload payload,
        Long senderId,
        MessageType messageType,
        Instant createdAt,
        Instant editedAt,
        Instant readAt,
        Set<Long> memberIds
) {}
