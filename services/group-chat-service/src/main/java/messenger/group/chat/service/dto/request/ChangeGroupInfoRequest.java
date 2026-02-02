package messenger.group.chat.service.dto.request;

public record ChangeGroupInfoRequest(
        String description,
        String avatarUrl,
        String name
) {}
