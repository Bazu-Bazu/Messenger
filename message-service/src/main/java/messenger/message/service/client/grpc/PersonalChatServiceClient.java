package messenger.message.service.client.grpc;

import exception.AuthorizationException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import personal_chat.PersonalChat;
import personal_chat.PersonalChatServiceGrpc;

import java.util.HashSet;
import java.util.Set;

@Component
public class PersonalChatServiceClient {

    @GrpcClient("personal-chat-service")
    private PersonalChatServiceGrpc.PersonalChatServiceBlockingStub blockingStub;

    public void validateUserIsPersonalChatMember(Long userId, Long chatId) {
        var request = PersonalChat.ValidateUserIsMemberOfPersonalChatRequest.newBuilder()
                .setUserId(userId)
                .setChatId(chatId)
                .build();

        var response = blockingStub.validateUserIsMemberOfPersonalChat(request);

        if (!response.getIsMember()) {
            throw new AuthorizationException(
                    String.format("User %d can not send message to personal chat %d", userId, chatId)
            );
        }
    }

    public Set<Long> getAllPersonalChatMembers(Long chatId) {
        var request = PersonalChat.GetAllPersonalChatMembersRequest.newBuilder()
                .setChatId(chatId)
                .build();

        var response = blockingStub.getAllPersonalChatMembers(request);

        return new HashSet<>(response.getUserIdList());
    }

}
