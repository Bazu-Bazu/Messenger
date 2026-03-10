package messenger.message.service.dto;

import lombok.Builder;

@Builder
public record MemberRightsInGroupDto(

        Long userId,
        Long chatId,
        boolean canSend,
        boolean canRead
) {}
