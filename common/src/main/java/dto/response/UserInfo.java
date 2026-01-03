package dto.response;

import lombok.Builder;

@Builder
public record UserInfo(
        Long id,
        String username,
        String avatarUrl,
        String status,
        String bio
) {}
