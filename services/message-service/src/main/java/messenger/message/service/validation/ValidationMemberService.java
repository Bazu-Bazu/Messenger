package messenger.message.service.validation;

import lombok.RequiredArgsConstructor;
import enums.ChatType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationMemberService {

    private final ChatValidationFactory chatValidationFactory;

    public void validateSending(Long userId, Long chatId, ChatType chatType) {
        chatValidationFactory.getStrategy(chatType).validateSending(userId, chatId);
    }

    public void validateReading(Long userId, Long chatId, ChatType chatType) {
        chatValidationFactory.getStrategy(chatType).validateReading(userId, chatId);
    }
}
