package messenger.message.service.validation;

import enums.ChatType;
import messenger.message.service.client.grpc.PersonalChatGrpcClient;
import messenger.message.service.dto.MemberRightsInPersonalChatDto;
import messenger.message.service.exception.AuthorizationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalChatSendValidationStrategyTest {

    @Mock
    private PersonalChatGrpcClient personalChatServiceClient;

    @InjectMocks
    private PersonalChatSendValidationStrategy strategy;

    @Test
    void getSupportedType_shouldReturnPersonal() {
        assertEquals(ChatType.PERSONAL, strategy.getSupportedType());
    }

    @Test
    void validateSending_whenUserIsMember_shouldPass() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInPersonalChatDto rights = mock(MemberRightsInPersonalChatDto.class);
        when(rights.isMember()).thenReturn(true);

        when(personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId))
                .thenReturn(rights);

        assertDoesNotThrow(() -> strategy.validateSending(userId, chatId));

        verify(personalChatServiceClient).getMemberRightsInPersonalChat(userId, chatId);
    }

    @Test
    void validateSending_whenUserIsNotMember_shouldThrowException() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInPersonalChatDto rights = mock(MemberRightsInPersonalChatDto.class);
        when(rights.isMember()).thenReturn(false);

        when(personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId))
                .thenReturn(rights);

        AuthorizationException exception = assertThrows(
                AuthorizationException.class,
                () -> strategy.validateSending(userId, chatId)
        );

        assertEquals(
                "User 1 cannot send messages in personal chat 10",
                exception.getMessage()
        );
    }

    @Test
    void validateReading_whenUserIsMember_shouldPass() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInPersonalChatDto rights = mock(MemberRightsInPersonalChatDto.class);
        when(rights.isMember()).thenReturn(true);

        when(personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId))
                .thenReturn(rights);

        assertDoesNotThrow(() -> strategy.validateReading(userId, chatId));

        verify(personalChatServiceClient).getMemberRightsInPersonalChat(userId, chatId);
    }

    @Test
    void validateReading_whenUserIsNotMember_shouldThrowException() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInPersonalChatDto rights = mock(MemberRightsInPersonalChatDto.class);
        when(rights.isMember()).thenReturn(false);

        when(personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId))
                .thenReturn(rights);

        AuthorizationException exception = assertThrows(
                AuthorizationException.class,
                () -> strategy.validateReading(userId, chatId)
        );

        assertEquals(
                "User 1 cannot read messages in personal chat 10",
                exception.getMessage()
        );
    }
}