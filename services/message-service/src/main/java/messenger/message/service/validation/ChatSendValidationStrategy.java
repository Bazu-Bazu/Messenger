package messenger.message.service.validation;

import enums.ChatType;

public interface ChatSendValidationStrategy {

    ChatType getSupportedType();
    void validateSending(Long userId, Long chatId);
    void validateReading(Long userId, Long chatId);
}
