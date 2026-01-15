package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupChatServiceClient;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.dto.request.EditMessageRequest;
import messenger.message.service.dto.request.GetMessagesRequest;
import messenger.message.service.dto.request.MarkMessageAsReadRequest;
import messenger.message.service.dto.request.SendMessageRequest;
import messenger.message.service.dto.response.MessageResponse;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.exception.MessageException;
import messenger.message.service.domain.repository.MessageRepository;
import messenger.message.service.client.kafka.MessageEventProducer;
import messenger.message.service.client.grpc.PersonalChatServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageEventProducer messageEventProducer;
    private final ValidationMemberService validationMemberService;
    private final GroupChatServiceClient groupChatServiceClient;
    private final PersonalChatServiceClient personalChatServiceClient;
    private final MessageCacheService messageCacheService;

    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request, Long senderId) {
        validationMemberService.validateOfSending(senderId, request.chatId(), request.chatType());

        Message newMessage = createAndSaveMessage(request, senderId);

        CompletableFuture.runAsync(() -> {
            notifyChatMembers(newMessage);
        });

        CompletableFuture.runAsync(() -> {
            messageCacheService.evictChatMessages(request.chatId(), request.chatType());
        });

        return createMessageResponse(newMessage);
    }

    public List<MessageResponse> getChatMessages(Long getterId, GetMessagesRequest request) {
        validationMemberService.validateOfGettingOrReading(getterId, request.chatId(), request.chatType());

        return messageCacheService.getCachedMessages(request.chatId(), request.chatType(), request.page());
    }

    @Transactional
    public MessageResponse editMessage(EditMessageRequest request, Long editorId) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new MessageException(
                        String.format("Message with id %d not found", request.messageId())
                ));

        validationMemberService.validateOfEditing(editorId, request.chatId(), request.chatType(), message);

        message.setContent(request.content());
        message.setMessageType(request.messageType());
        message.setEditedAt(Instant.now());
        Message updatedMessage = messageRepository.save(message);

        CompletableFuture.runAsync(() -> {
            messageCacheService.evictChatMessages(request.chatId(), request.chatType());
        });

        return createMessageResponse(updatedMessage);
    }

    @Transactional
    public MessageResponse markMessageAsRead(Long readerId, MarkMessageAsReadRequest request) {
        Message message = messageRepository.findByIdAndChatIdAndChatType(
                request.messageId(), request.chatId(), request.chatType()
                )
                .orElseThrow(() -> new MessageException(
                        String.format("Message with id %d and %s chat %d not found",
                                request.messageId(), request.chatType(), request.chatId())
                ));

        validationMemberService.validateOfGettingOrReading(readerId, request.chatId(), request.chatType());

        if (message.getReadAt() == null && !message.getSenderId().equals(readerId)) {
            message.setReadAt(Instant.now());
            Message updatedMessage = messageRepository.save(message);
            CompletableFuture.runAsync(() -> {
                messageCacheService.evictChatMessages(request.chatId(), request.chatType());
            });

            return createMessageResponse(updatedMessage);
        }

        return createMessageResponse(message);
    }

    private Message createAndSaveMessage(SendMessageRequest request, Long senderId) {
        Message message = Message.builder()
                .chatId(request.chatId())
                .senderId(senderId)
                .content(request.content())
                .messageType(request.messageType())
                .chatType(request.chatType())
                .createdAt(Instant.now())
                .build();

        return messageRepository.save(message);
    }

    private MessageResponse createMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .chatId(message.getChatId())
                .createdAt(message.getCreatedAt())
                .editedAt(message.getEditedAt())
                .readAt(message.getReadAt())
                .messageType(message.getMessageType())
                .chatType(message.getChatType())
                .build();
    }

    private void notifyChatMembers(Message message) {
        Set<Long> memberIds;
        if (message.getChatType().equals(ChatType.PERSONAL)) {
            memberIds = personalChatServiceClient.getAllPersonalChatMembers(message.getChatId());
        } else {
            memberIds = groupChatServiceClient.getAllGroupChatMembers(message.getChatId());
        }
        memberIds.remove(message.getSenderId());

        MessageResponse messageResponse = createMessageResponse(message);
        messageEventProducer.publishMessageNotification(messageResponse, memberIds);
    }

}
