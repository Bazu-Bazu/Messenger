package dto.event;

import enums.SavedChatEventType;
import lombok.Builder;

@Builder
public record SavedChatEvent(

        Long id,
        SavedChatEventType eventType,
        Long userId
) {}
