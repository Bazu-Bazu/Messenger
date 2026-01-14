package messenger.message.service.client.grpc;

import exception.AuthorizationException;
import group_chat.GroupChat;
import group_chat.GroupChatServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GroupChatServiceClient {

    @GrpcClient("group-chat-service")
    private GroupChatServiceGrpc.GroupChatServiceBlockingStub blockingStub;

    private GroupChat.ValidateMemberRightsInGroupChatRequestResponse validateMemberRightsInGroupChat(
            Long userId,
            Long chatId
    ) {
        var request = GroupChat.ValidateMemberRightsInGroupChatRequest.newBuilder()
                .setUserId(userId)
                .setChatId(chatId)
                .build();

        return blockingStub.validateMemberRightsInGroupChat(request);
    }

    public void validateUserCanSendMessage(Long userId, Long chatId) {
        var response = validateMemberRightsInGroupChat(userId, chatId);

        if (!response.getCanSendMessage()) {
            throw new AuthorizationException(
                    String.format("User %d can not send message in group chat %d", userId, chatId)
            );
        }
    }

    public void validateUserCanGetMessages(Long userId, Long chatId) {
        var response = validateMemberRightsInGroupChat(userId, chatId);

        if (!response.getCanGetMessage()) {
            throw new AuthorizationException(
                    String.format("User %d can not get messages from group chat %d", userId, chatId)
            );
        }
    }

    public Set<Long> getAllGroupChatMembers(Long chatId) {
        var request = GroupChat.GetAllGroupChatMembersRequest.newBuilder()
                .setChatId(chatId)
                .build();

        var response = blockingStub.getAllGroupChatMembers(request);

        return new HashSet<>(response.getUserIdList());
    }

}
