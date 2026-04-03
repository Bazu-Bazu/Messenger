package dto.event;

import lombok.Builder;

@Builder
public record PersonalChatEvent(

        Long id
) {}
