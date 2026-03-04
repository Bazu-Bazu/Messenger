package messenger.personal.chat.service.kafka.consumer;

import dto.event.MessageShortEvent;
import enums.ChatType;
import messenger.personal.chat.service.service.PersonalChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class MessageEventHandlerTest {

    @Mock
    private PersonalChatService personalChatService;

    @InjectMocks
    private MessageEventHandler messageEventHandler;

    @Test
    void shouldCallUpdateLastActivityWhenEventReceived() {
        Long chatId = 10L;
        Instant createdAt = Instant.now();

        MessageShortEvent event = new MessageShortEvent(
                1L,
                chatId,
                ChatType.PERSONAL,
                createdAt
        );

        messageEventHandler.updatePersonalChatLastActivity(event);

        verify(personalChatService)
                .updateLastActivity(chatId, createdAt);

        verifyNoMoreInteractions(personalChatService);
    }
}