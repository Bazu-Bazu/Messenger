package messenger.group.chat.service.dto;

import lombok.Builder;

@Builder
public record UserExistenceDto(

        Long userId,
        boolean exists,
        boolean isActive
) {}