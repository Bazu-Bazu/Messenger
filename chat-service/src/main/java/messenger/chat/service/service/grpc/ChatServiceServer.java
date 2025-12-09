package messenger.chat.service.service.grpc;

import com.messenger.grpc.Chat;
import com.messenger.grpc.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.entity.ChatMember;
import messenger.chat.service.entity.ChatMemberRole;
import messenger.chat.service.repository.ChatMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceServer extends ChatServiceGrpc.ChatServiceImplBase {

    private final ChatMemberRepository chatMemberRepository;

    @Override
    public void validateUserCanSendMessage(Chat.ValidateUserCanSendMessageRequest request,
                                           StreamObserver<Chat.ValidateUserCanSendMessageResponse> responseObserver
    ) {
        Optional<ChatMember> member = chatMemberRepository
                .findByChatIdAndUserId(request.getChatId(), request.getSenderId());

        if (member.isEmpty() || member.get().getRole() == ChatMemberRole.READONLY) {
            sendValidateUserCanSendMessageResponse(false, responseObserver);
        } else {
            sendValidateUserCanSendMessageResponse(true, responseObserver);
        }
    }

    private void sendValidateUserCanSendMessageResponse(boolean userCanSendMessage,
                                            StreamObserver<Chat.ValidateUserCanSendMessageResponse> responseObserver
    ) {
        Chat.ValidateUserCanSendMessageResponse response = Chat.ValidateUserCanSendMessageResponse.newBuilder()
                .setUserCanSendMessage(userCanSendMessage)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getChatMembersIds(Chat.GetChatMembersIdsRequest request,
                                  StreamObserver<Chat.GetChatMembersIdsResponse> responseObserver
    ) {
        List<ChatMember> chatMembers = chatMemberRepository.findByChatId(request.getChatId());

        Chat.GetChatMembersIdsResponse.Builder responseBuilder = Chat.GetChatMembersIdsResponse.newBuilder();

        chatMembers.stream()
                .map(ChatMember::getUserId)
                .forEach(responseBuilder::addMemberIds);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void validateUserIsChatMember(Chat.ValidateUserIsChatMemberRequest request,
                                         StreamObserver<Chat.ValidateUserIsChatMemberResponse> responseObserver
    ) {
        boolean userIsChatMember = chatMemberRepository
                .existByChatIdAndUserId(request.getChatId(), request.getUserId());

        Chat.ValidateUserIsChatMemberResponse response = Chat.ValidateUserIsChatMemberResponse.newBuilder()
                .setIsChatMember(userIsChatMember)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
