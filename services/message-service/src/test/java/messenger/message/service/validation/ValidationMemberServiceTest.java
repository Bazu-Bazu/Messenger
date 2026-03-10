package messenger.message.service.validation;

import enums.ChatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationMemberServiceTest {

    @Mock
    private ChatValidationFactory chatValidationFactory;

    @Mock
    private ChatSendValidationStrategy strategy;

    @InjectMocks
    private ValidationMemberService validationMemberService;

    @Test
    void validateSending_shouldCallStrategyValidateSending() {
        Long userId = 1L;
        Long chatId = 10L;
        ChatType chatType = ChatType.GROUP;

        when(chatValidationFactory.getStrategy(chatType)).thenReturn(strategy);

        validationMemberService.validateSending(userId, chatId, chatType);

        verify(chatValidationFactory).getStrategy(chatType);
        verify(strategy).validateSending(userId, chatId);
    }

    @Test
    void validateReading_shouldCallStrategyValidateReading() {
        Long userId = 1L;
        Long chatId = 10L;
        ChatType chatType = ChatType.PERSONAL;

        when(chatValidationFactory.getStrategy(chatType)).thenReturn(strategy);

        validationMemberService.validateReading(userId, chatId, chatType);

        verify(chatValidationFactory).getStrategy(chatType);
        verify(strategy).validateReading(userId, chatId);
    }
}
