package messenger.message.service.validation;

import enums.ChatType;
import messenger.message.service.client.grpc.GroupGrpcClient;
import messenger.message.service.dto.MemberRightsInGroupDto;
import messenger.message.service.exception.AuthorizationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupSendValidationStrategyTest {

    @Mock
    private GroupGrpcClient groupChatServiceClient;

    @InjectMocks
    private GroupSendValidationStrategy strategy;

    @Test
    void getSupportedType_shouldReturnGroup() {
        assertEquals(ChatType.GROUP, strategy.getSupportedType());
    }

    @Test
    void validateSending_whenUserCanSend_shouldPass() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInGroupDto rights = mock(MemberRightsInGroupDto.class);
        when(rights.canSend()).thenReturn(true);

        when(groupChatServiceClient.getMemberRightsInGroup(userId, chatId))
                .thenReturn(rights);

        assertDoesNotThrow(() -> strategy.validateSending(userId, chatId));

        verify(groupChatServiceClient).getMemberRightsInGroup(userId, chatId);
    }

    @Test
    void validateSending_whenUserCannotSend_shouldThrowException() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInGroupDto rights = mock(MemberRightsInGroupDto.class);
        when(rights.canSend()).thenReturn(false);

        when(groupChatServiceClient.getMemberRightsInGroup(userId, chatId))
                .thenReturn(rights);

        AuthorizationException ex = assertThrows(
                AuthorizationException.class,
                () -> strategy.validateSending(userId, chatId)
        );

        assertEquals(
                "User 1 cannot send messages in group 10",
                ex.getMessage()
        );
    }

    @Test
    void validateReading_whenUserCanRead_shouldPass() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInGroupDto rights = mock(MemberRightsInGroupDto.class);
        when(rights.canRead()).thenReturn(true);

        when(groupChatServiceClient.getMemberRightsInGroup(userId, chatId))
                .thenReturn(rights);

        assertDoesNotThrow(() -> strategy.validateReading(userId, chatId));

        verify(groupChatServiceClient).getMemberRightsInGroup(userId, chatId);
    }

    @Test
    void validateReading_whenUserCannotRead_shouldThrowException() {
        Long userId = 1L;
        Long chatId = 10L;

        MemberRightsInGroupDto rights = mock(MemberRightsInGroupDto.class);
        when(rights.canRead()).thenReturn(false);

        when(groupChatServiceClient.getMemberRightsInGroup(userId, chatId))
                .thenReturn(rights);

        AuthorizationException ex = assertThrows(
                AuthorizationException.class,
                () -> strategy.validateReading(userId, chatId)
        );

        assertEquals(
                "User 1 cannot read messages in group 10",
                ex.getMessage()
        );
    }
}
