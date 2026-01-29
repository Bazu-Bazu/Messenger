package messenger.message.service.service;

import exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupChatServiceClient;
import messenger.message.service.client.grpc.PersonalChatServiceClient;
import messenger.message.service.client.grpc.UserGrpcClient;
import messenger.message.service.domain.entity.Message;
import enums.ChatType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ValidationMemberService {

    private final UserGrpcClient userGrpcClient;
    private final PersonalChatServiceClient personalChatServiceClient;
    private final GroupChatServiceClient groupChatServiceClient;

    public void validateOfSending(Long userId, Long chatId, ChatType chatType) {
        CompletableFuture<Void> userValidation = CompletableFuture.runAsync(() ->
                userGrpcClient.validateUsersExist(List.of(userId))
        );

        CompletableFuture<Void> chatValidation = CompletableFuture.runAsync(() -> {
            if (chatType.equals(ChatType.PERSONAL)) {
                personalChatServiceClient.validateUserIsPersonalChatMember(userId, chatId);
            } else {
                groupChatServiceClient.validateUserCanSendMessage(userId, chatId);
            }
        });

        CompletableFuture.allOf(userValidation, chatValidation).join();
    }

    public void validateOfGettingOrReading(Long userId, Long chatId, ChatType chatType) {
        CompletableFuture<Void> userValidation = CompletableFuture.runAsync(() ->
                userGrpcClient.validateUsersExist(List.of(userId))
        );

        CompletableFuture<Void> chatValidation = CompletableFuture.runAsync(() -> {
            if (chatType.equals(ChatType.PERSONAL)) {
                personalChatServiceClient.validateUserIsPersonalChatMember(userId, chatId);
            } else {
                groupChatServiceClient.validateUserCanGetAndReadMessages(userId, chatId);
            }
        });

        CompletableFuture.allOf(userValidation, chatValidation).join();
    }

    public void validateOfEditing(Long userId, Long chatId, ChatType chatType, Message message) {
        validateOfSending(userId, chatId, chatType);

        if (!message.getSenderId().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d cannot edit message %d in %s chat %d",
                            userId, message.getId(), chatType, chatId)
            );
        }
    }

}
