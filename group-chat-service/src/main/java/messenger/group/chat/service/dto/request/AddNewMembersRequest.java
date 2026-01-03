package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AddNewMembersRequest(
        @NotEmpty
        @Size(max = 50)
        List<Long> userIds,

        Long groupId
) {}
