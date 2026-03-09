package messenger.group.chat.service.kafka.consumer;

import dto.event.MessageShortEvent;
import enums.ChatType;
import messenger.group.chat.service.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageEventHandlerTest {

    @Mock
    private GroupService groupChatService;

    @InjectMocks
    private MessageEventHandler handler;

    @Test
    void updateGroupChatLastActivity_shouldCallService() {
        MessageShortEvent event = new MessageShortEvent(
                10L,
                100L,
                ChatType.GROUP,
                Instant.now()
        );

        handler.updateGroupChatLastActivity(event);

        verify(groupChatService)
                .updateLastActivity(event.chatId(), event.createdAt());
    }
}
