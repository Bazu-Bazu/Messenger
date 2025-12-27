package messenger.chat.service.dto.request;

import java.util.List;

public record CreateGroupChatResponse (
    String name,
    List<Long> userIds
) {}
