package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public record AddNewMembersRequest(

        @NotEmpty(message = "The users must be specified")
        @Size(max = 50, message = "You can add a maximum of 50 users at a time")
        List<Long> userIds
) {

    public AddNewMembersRequest {
        userIds = userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
