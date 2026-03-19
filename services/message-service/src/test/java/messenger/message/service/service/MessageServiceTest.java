package messenger.message.service.service;

import dto.payload.TextPayload;
import dto.request.GetMessagesRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.MessageResponse;
import enums.ChatType;
import enums.MessageType;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.domain.repository.MessageRepository;
import messenger.message.service.exception.MessageNotFoundException;
import messenger.message.service.mapper.MessageMapper;
import messenger.message.service.validation.ValidationMemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ValidationMemberService validationMemberService;

    @Mock
    private MessageQueryService messageQueryService;

    @Mock
    private MessageCacheInvalidationService messageCacheInvalidationService;

    @Mock
    private NotifyService notifyService;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @Test
    void sendMessage_shouldValidateSend_notifyAndInvalidateCache_returnResponse() {
        Long senderId = 1L;
        SendMessageRequest request = mock(SendMessageRequest.class);
        Message message = mock(Message.class);
        MessageResponse response = mock(MessageResponse.class);

        TextPayload payload = new TextPayload("Hello");

        when(request.chatId()).thenReturn(10L);
        when(request.chatType()).thenReturn(ChatType.GROUP);
        when(request.payload()).thenReturn(payload);
        when(request.messageType()).thenReturn(MessageType.TEXT);

        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.toMessageResponse(message)).thenReturn(response);

        MessageResponse result = messageService.sendMessage(request, senderId);

        verify(validationMemberService).validateSending(senderId, 10L, ChatType.GROUP);
        verify(messageRepository).save(messageCaptor.capture());
        verify(notifyService).notifyChatMembers(message);
        verify(messageCacheInvalidationService).evictChatMessages(10L, ChatType.GROUP);
        verify(messageMapper).toMessageResponse(message);

        Message savedMessage = messageCaptor.getValue();
        assertEquals(senderId, savedMessage.getSenderId());
        assertEquals("Hello", savedMessage.getText());
        assertNull(savedMessage.getMediaId());
        assertEquals(ChatType.GROUP, savedMessage.getChatType());

        assertEquals(response, result);
    }

    @Test
    void getChatMessages_shouldValidateReading_returnCachedMessages() {
        Long getterId = 2L;
        GetMessagesRequest request = mock(GetMessagesRequest.class);
        List<MessageResponse> cachedMessages = List.of(mock(MessageResponse.class));

        when(request.chatId()).thenReturn(10L);
        when(request.chatType()).thenReturn(ChatType.PERSONAL);
        when(request.page()).thenReturn(0);
        when(messageQueryService.getCachedMessages(10L, ChatType.PERSONAL, 0))
                .thenReturn(cachedMessages);

        List<MessageResponse> result = messageService.getChatMessages(getterId, request);

        verify(validationMemberService).validateReading(getterId, 10L, ChatType.PERSONAL);
        verify(messageQueryService).getCachedMessages(10L, ChatType.PERSONAL, 0);

        assertEquals(cachedMessages, result);
    }

    @Test
    void markMessageAsRead_shouldValidateReading_markMessage_returnResponse() {
        Long readerId = 2L;
        MarkMessageAsReadRequest request = mock(MarkMessageAsReadRequest.class);
        Message savedMessage = mock(Message.class);
        MessageResponse response = mock(MessageResponse.class);

        when(request.chatId()).thenReturn(10L);
        when(request.chatType()).thenReturn(ChatType.GROUP);
        when(request.messageId()).thenReturn(100L);
        when(messageRepository.findById(100L)).thenReturn(Optional.of(savedMessage));
        when(messageMapper.toMessageResponse(savedMessage)).thenReturn(response);

        MessageResponse result = messageService.markMessageAsRead(readerId, request);

        verify(validationMemberService).validateReading(readerId, 10L, ChatType.GROUP);
        verify(messageRepository).markAsRead(eq(100L), any(Instant.class));
        verify(messageRepository).findById(100L);
        verify(messageMapper).toMessageResponse(savedMessage);

        assertEquals(response, result);
    }

    @Test
    void markMessageAsRead_messageNotFound_shouldThrowException() {
        Long readerId = 2L;
        MarkMessageAsReadRequest request = mock(MarkMessageAsReadRequest.class);

        when(request.chatId()).thenReturn(10L);
        when(request.chatType()).thenReturn(ChatType.GROUP);
        when(request.messageId()).thenReturn(100L);
        when(messageRepository.findById(100L)).thenReturn(Optional.empty());

        MessageNotFoundException ex = assertThrows(
                MessageNotFoundException.class,
                () -> messageService.markMessageAsRead(readerId, request)
        );

        assertEquals("Message 100 not found", ex.getMessage());
    }
}
