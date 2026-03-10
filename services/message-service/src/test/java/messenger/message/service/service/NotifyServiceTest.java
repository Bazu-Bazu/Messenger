package messenger.message.service.service;

import enums.ChatType;
import messenger.message.service.client.grpc.GroupGrpcClient;
import messenger.message.service.client.grpc.PersonalChatGrpcClient;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.kafka.producer.MessageEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotifyServiceTest {

    @Mock
    private GroupGrpcClient groupChatServiceClient;

    @Mock
    private PersonalChatGrpcClient personalChatServiceClient;

    @Mock
    private MessageEventProducer messageEventProducer;

    @InjectMocks
    private NotifyService notifyService;

    @Test
    void notifyChatMembers_personalChat_shouldSendNotificationWithoutSender() {
        Long chatId = 10L;
        Long senderId = 1L;

        Message message = mock(Message.class);

        when(message.getChatId()).thenReturn(chatId);
        when(message.getSenderId()).thenReturn(senderId);
        when(message.getChatType()).thenReturn(ChatType.PERSONAL);

        Set<Long> members = new HashSet<>(Set.of(1L, 2L, 3L));

        when(personalChatServiceClient.getAllPersonalChatMembers(chatId))
                .thenReturn(members);

        notifyService.notifyChatMembers(message);

        verify(personalChatServiceClient).getAllPersonalChatMembers(chatId);
        verify(messageEventProducer)
                .publishMessageNotification(message, Set.of(2L, 3L));
    }

    @Test
    void notifyChatMembers_groupChat_shouldSendNotificationWithoutSender() {
        Long chatId = 20L;
        Long senderId = 5L;

        Message message = mock(Message.class);

        when(message.getChatId()).thenReturn(chatId);
        when(message.getSenderId()).thenReturn(senderId);
        when(message.getChatType()).thenReturn(ChatType.GROUP);

        Set<Long> members = new HashSet<>(Set.of(5L, 6L, 7L));

        when(groupChatServiceClient.getAllGroupChatMembers(chatId))
                .thenReturn(members);

        notifyService.notifyChatMembers(message);

        verify(groupChatServiceClient).getAllGroupChatMembers(chatId);
        verify(messageEventProducer)
                .publishMessageNotification(message, Set.of(6L, 7L));
    }
}
