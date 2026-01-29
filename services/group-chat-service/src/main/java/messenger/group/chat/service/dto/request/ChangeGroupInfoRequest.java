package messenger.group.chat.service.dto.request;

public record ChangeGroupInfoRequest(
        Long groupId,
        String description,
        String avatarUrl,
        String name
) {}
