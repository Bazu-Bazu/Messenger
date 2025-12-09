package messenger.message.service.dto.request;

import messenger.message.service.entity.MessageType;

public record SendMessageRequest (
        Long chatId,
        String content,
        MessageType type
) {}
