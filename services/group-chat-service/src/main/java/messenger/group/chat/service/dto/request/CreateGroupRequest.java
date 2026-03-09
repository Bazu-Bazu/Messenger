package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public record CreateGroupRequest(

        @NotBlank(message = "Group name cannot be empty")
        @Size(min = 1, max = 40, message = "Group name must be 1-40 characters")
        String name,

        @Size(max = 200, message = "Description should not exceed 200 characters")
        String description,

        @Size(max = 50, message = "You can add a maximum of 50 users at a time")
        List<Long> userIds,

        @Size(max = 255, message = "URL must not exceed 255 characters")
        @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http or https")
        String avatarUrl
) {

    public CreateGroupRequest {
        userIds = userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
