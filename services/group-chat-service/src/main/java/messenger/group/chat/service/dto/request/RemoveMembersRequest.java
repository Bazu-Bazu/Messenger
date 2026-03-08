package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RemoveMembersRequest(

        @NotEmpty(message = "The users must be specified")
        @Size(max = 50, message = "You can delete a maximum of 50 members at a time")
        List<Long> userIds
) {}
