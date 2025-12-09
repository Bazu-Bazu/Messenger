package messenger.message.service.dto.request;

import messenger.message.service.entity.MessageType;

public record EditMessageRequest (
        Long messageId,
        Long chatId,
        String content,
        MessageType type
) {}
