package messenger.group.chat.service.dto.response;

import dto.response.UserInfo;
import lombok.Builder;
import messenger.group.chat.service.domain.enums.GroupMemberRole;

@Builder
public record GroupMemberResponse(
        Long id,
        String customNickname,
        GroupMemberRole role,
        UserInfo userInfo
) {}
