package messenger.message.service.dto;

import lombok.Builder;

@Builder
public record MemberRightsInPersonalChatDto(

        Long userId,
        Long chatId,
        boolean isMember
) {}
