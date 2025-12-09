package messenger.message.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import messenger.message.service.dto.request.EditMessageRequest;
import messenger.message.service.dto.request.SendMessageRequest;
import messenger.message.service.dto.response.MessageResponse;
import messenger.message.service.entity.Message;
import messenger.message.service.exception.MessageException;
import messenger.message.service.repository.MessageRepository;
import messenger.message.service.service.event.MessageEventProducer;
import messenger.message.service.service.grpc.ChatServiceClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageEventProducer messageEventProducer;
    private final ChatServiceClient chatServiceClient;

    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request, Long senderId) {
//        chatServiceClient.validateUserCanSendMessage(request.chatId(), senderId);

        Message newMessage = createAndSaveMessage(request, senderId);

        CompletableFuture.runAsync(() -> {
            messageEventProducer.publishMessageSent(newMessage);
            notifyChatMembers(newMessage);
        });

        return createMessageResponse(newMessage);
    }

    @Cacheable(value = "chatMessages", key = "#chatId + ':' + #page")
    public List<MessageResponse> getChatMessages(Long chatId, int page, int size) {
//        chatServiceClient.validateUserIsChatMember(userId);

        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("created_at").descending());
        return messageRepository.findByChatId(chatId, pageable).stream()
                .map(this::createMessageResponse)
                .toList();
    }

    @CacheEvict(value = "chatMessages", key = "#request.chatId()")
    public MessageResponse editMessage(EditMessageRequest request, Long senderId) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new MessageException(
                        String.format("Message with id %d not found", request.messageId())
                ));

        message.setContent(request.content());
        message.setType(request.type());
        message.setEditedAt(Instant.now());

        Message updatedMessage = messageRepository.save(message);

        CompletableFuture.runAsync(() -> {
            messageEventProducer.publishMessageEdited(updatedMessage);
        });

        return createMessageResponse(updatedMessage);
    }

    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageException(
                        String.format("Message with id %d not found", messageId)
                ));

        message.setReadAt(Instant.now());
        Message updatedMessage = messageRepository.save(message);

        messageEventProducer.publishMessageRead(updatedMessage);
    }

    private Message createAndSaveMessage(SendMessageRequest request, Long senderId) {
        Message message = Message.builder()
                .chatId(request.chatId())
                .senderId(senderId)
                .content(request.content())
                .type(request.type())
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
                .type(message.getType())
                .build();
    }

    private void notifyChatMembers(Message message) {
        List<Long> memberIds = chatServiceClient.getChatMembersIds(message.getChatId()).stream()
                .filter(memberId -> !memberId.equals(message.getSenderId()))
                .toList();

        messageEventProducer.publishMessageNotification(message, memberIds);
    }

}
