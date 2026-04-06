package dto.event;

import enums.PersonalChatEventType;
import lombok.Builder;

@Builder
public record PersonalChatEvent(

        Long id,
        PersonalChatEventType eventType,
        Long user1Id,
        Long user2Id
) {}
