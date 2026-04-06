package dto.event;

import enums.ChatMemberRole;
import enums.GroupChatEventType;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupChatEvent(

        Long id,
        GroupChatEventType eventType,
        String name,
        Long avatarId,
        Long ownerId,
        GroupMembers members
) {

    public record GroupMembers(

         List<Long> userIds,
         ChatMemberRole role
    ) {}
}
