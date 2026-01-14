package messenger.group.chat.service.controller.grpc;

import group_chat.GroupChat;
import group_chat.GroupChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.repository.GroupChatMemberRepository;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class GroupChatGrpcService extends GroupChatServiceGrpc.GroupChatServiceImplBase {

    private final GroupChatMemberRepository groupChatMemberRepository;

    @Override
    public void validateMemberRightsInGroupChat(
            GroupChat.ValidateMemberRightsInGroupChatRequest request,
            StreamObserver<GroupChat.ValidateMemberRightsInGroupChatRequestResponse> responseObserver
    ) {
        Optional<GroupChatMember> member = groupChatMemberRepository.findByGroupIdAndUserId(
                request.getChatId(), request.getUserId()
        );

        boolean canSendMessage = member.isPresent() && member.get().canSendMessage();
        boolean canGetMessages = member.isPresent();

        var response = GroupChat.ValidateMemberRightsInGroupChatRequestResponse.newBuilder()
                .setUserId(request.getUserId())
                .setChatId(request.getChatId())
                .setCanGetMessage(canSendMessage)
                .setCanGetMessage(canGetMessages)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
