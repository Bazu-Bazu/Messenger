package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import messenger.group.chat.service.domain.enums.GroupMemberRole;

import java.util.List;

public record SetRolesRequest(

        @NotEmpty(message = "The users must be specified")
        @Size(max = 50, message = "You can set role a maximum of 50 members at a time")
        List<Long> userIds,

        @NotNull(message = "The role cannot be empty")
        GroupMemberRole role
) {}
