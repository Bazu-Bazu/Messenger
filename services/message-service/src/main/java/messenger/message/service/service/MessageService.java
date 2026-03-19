package messenger.message.service.service;

import dto.payload.MediaPayload;
import dto.payload.TextPayload;
import lombok.RequiredArgsConstructor;
import dto.request.GetMessagesRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.MessageResponse;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.domain.repository.MessageRepository;
import messenger.message.service.exception.IllegalPayloadTypeException;
import messenger.message.service.exception.MessageNotFoundException;
import messenger.message.service.mapper.MessageMapper;
import messenger.message.service.validation.ValidationMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ValidationMemberService validationMemberService;
    private final MessageQueryService messageQueryService;
    private final MessageCacheInvalidationService messageCacheInvalidationService;
    private final NotifyService notifyService;
    private final MessageMapper messageMapper;

    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request, Long senderId) {
        validationMemberService.validateSending(senderId, request.chatId(), request.chatType());

        Message newMessage = createAndSaveMessage(request, senderId);

        notifyService.notifyChatMembers(newMessage);

        messageCacheInvalidationService.evictChatMessages(request.chatId(), request.chatType());

        return messageMapper.toMessageResponse(newMessage);
    }

    public List<MessageResponse> getChatMessages(Long getterId, GetMessagesRequest request) {
        validationMemberService.validateReading(getterId, request.chatId(), request.chatType());

        return messageQueryService.getCachedMessages(request.chatId(), request.chatType(), request.page());
    }

    @Transactional
    public MessageResponse markMessageAsRead(Long readerId, MarkMessageAsReadRequest request) {
        validationMemberService.validateReading(readerId, request.chatId(), request.chatType());

        messageRepository.markAsRead(request.messageId(), Instant.now());

        Message savedMessage = findMessageById(request.messageId());

        return messageMapper.toMessageResponse(savedMessage);
    }

    private Message findMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(
                        String.format("Message %d not found", messageId)
                ));
    }

    private Message createAndSaveMessage(SendMessageRequest request, Long senderId) {
        Message message = Message.builder()
                .chatId(request.chatId())
                .senderId(senderId)
                .messageType(request.messageType())
                .chatType(request.chatType())
                .createdAt(Instant.now())
                .build();

        applyPayload(message, request);

        return messageRepository.save(message);
    }

    private void applyPayload(Message message, SendMessageRequest request) {
        var payload = request.payload();

        if (payload instanceof TextPayload t) {
            message.setText(t.text());
        } else if (payload instanceof MediaPayload m) {
            message.setMediaId(m.mediaId());
        } else {
            throw new IllegalPayloadTypeException("Unknown payload type: " + payload.getClass());
        }
    }
}
