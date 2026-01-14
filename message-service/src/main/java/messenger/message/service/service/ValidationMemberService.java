package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupChatServiceClient;
import messenger.message.service.client.grpc.PersonalChatServiceClient;
import messenger.message.service.client.grpc.UserGrpcClient;
import messenger.message.service.domain.enums.ChatType;
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

}
