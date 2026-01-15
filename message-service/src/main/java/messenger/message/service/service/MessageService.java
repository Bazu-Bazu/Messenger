package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupChatServiceClient;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.dto.request.EditMessageRequest;
import messenger.message.service.dto.request.GetMessagesRequest;
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

        return createMessageResponse(newMessage);
    }

    public List<MessageResponse> getChatMessages(Long getterId, GetMessagesRequest request) {
        validationMemberService.validateOfGetting(getterId, request.chatId(), request.chatType());

        return messageCacheService.getCachedMessages(request.chatId(), request.chatType(), request.page());
    }

//    @CacheEvict(value = "chatMessages", key = "#request.chatId()")
//    public MessageResponse editMessage(EditMessageRequest request, Long senderId) {
//        Message message = messageRepository.findById(request.messageId())
//                .orElseThrow(() -> new MessageException(
//                        String.format("Message with id %d not found", request.messageId())
//                ));
//
//        if (!message.getSenderId().equals(senderId)) {
//            throw new RightsException("User cannot edit this message");
//        }
//
//        message.setContent(request.content());
//        message.setType(request.type());
//        message.setEditedAt(Instant.now());
//
//        Message updatedMessage = messageRepository.save(message);
//
//        CompletableFuture.runAsync(() -> {
//            messageEventProducer.publishMessageEdited(updatedMessage);
//        });
//
//        return createMessageResponse(updatedMessage);
//    }
//
//    public void markMessageAsRead(Long messageId) {
//        Message message = messageRepository.findById(messageId)
//                .orElseThrow(() -> new MessageException(
//                        String.format("Message with id %d not found", messageId)
//                ));
//
//        message.setReadAt(Instant.now());
//        Message updatedMessage = messageRepository.save(message);
//
//        messageEventProducer.publishMessageRead(updatedMessage);
//    }

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
