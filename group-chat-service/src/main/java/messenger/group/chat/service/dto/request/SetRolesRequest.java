package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import messenger.group.chat.service.domain.enums.GroupMemberRole;

import java.util.List;

public record SetRolesRequest(
        @NotEmpty
        @Size(max = 50)
        List<Long> userIds,

        Long groupId,
        GroupMemberRole role
) {}
