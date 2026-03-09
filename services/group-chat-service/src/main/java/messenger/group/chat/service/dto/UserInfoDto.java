package messenger.group.chat.service.dto;

import lombok.Builder;

@Builder
public record UserInfoDto(

        Long userId,
        String username,
        String avatarUrl
) {}
