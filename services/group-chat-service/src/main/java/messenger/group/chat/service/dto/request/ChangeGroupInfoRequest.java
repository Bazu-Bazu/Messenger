package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.Size;

public record ChangeGroupInfoRequest(

        @Size(max = 200, message = "Description should not exceed 200 characters")
        String description,

        Long avatarId,

        @Size(min = 1, max = 40, message = "Group name must be 1-40 characters")
        String name
) {}
