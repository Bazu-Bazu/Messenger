package messenger.group.chat.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateGroupChatRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @Size(max = 200)
        String description,

        @Size(max = 50)
        List<Long> userIds,

        String avatarUrl
) {}
