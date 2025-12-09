package messenger.message.service.service.grpc;

import com.messenger.grpc.Chat;
import com.messenger.grpc.ChatServiceGrpc;
import messenger.message.service.exception.RightsException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceClient {

    @GrpcClient("chat-service")
    private ChatServiceGrpc.ChatServiceBlockingStub blockingStub;

    public void validateUserCanSendMessage(Long chatId, Long senderId) {
        Chat.ValidateUserCanSendMessageRequest request = Chat.ValidateUserCanSendMessageRequest.newBuilder()
                .setSenderId(senderId)
                .setChatId(chatId)
                .build();

        Chat.ValidateUserCanSendMessageResponse response = blockingStub.validateUserCanSendMessage(request);

        if (!response.getUserCanSendMessage()) {
            throw new RightsException("User cannot send a massage to the chat");
        }
    }

    public List<Long> getChatMembersIds(Long chatId) {
        Chat.GetChatMembersIdsRequest request = Chat.GetChatMembersIdsRequest.newBuilder()
                .setChatId(chatId)
                .build();

        Chat.GetChatMembersIdsResponse response = blockingStub.getChatMembersIds(request);

        return response.getMemberIdsList();
    }

    public void validateUserIsChatMember(Long chatId, Long userId) {
        Chat.ValidateUserIsChatMemberRequest request = Chat.ValidateUserIsChatMemberRequest.newBuilder()
                .setUserId(userId)
                .setChatId(chatId)
                .build();

        Chat.ValidateUserIsChatMemberResponse response = blockingStub.validateUserIsChatMember(request);

        if (!response.getIsChatMember()) {
            throw new RightsException("User is not a member of the chat");
        }
    }

}
