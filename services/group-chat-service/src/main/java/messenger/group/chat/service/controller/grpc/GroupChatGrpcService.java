package messenger.group.chat.service.controller.grpc;

import group_chat.GroupChat;
import group_chat.GroupChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.service.GroupService;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;
import java.util.Set;

@GrpcService
@RequiredArgsConstructor
public class GroupChatGrpcService extends GroupChatServiceGrpc.GroupChatServiceImplBase {

    private final GroupService groupService;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public void validateMemberRightsInGroupChat(
            GroupChat.ValidateMemberRightsInGroupChatRequest request,
            StreamObserver<GroupChat.ValidateMemberRightsInGroupChatRequestResponse> responseObserver
    ) {
        Optional<GroupChatMember> member = groupMemberRepository.findByGroupIdAndUserId(
                request.getChatId(), request.getUserId()
        );

        boolean canSendMessage = member.isPresent() && member.get().canSendMessage();
        boolean canGetMessages = member.isPresent();

        var response = GroupChat.ValidateMemberRightsInGroupChatRequestResponse.newBuilder()
                .setUserId(request.getUserId())
                .setChatId(request.getChatId())
                .setCanSendMessage(canSendMessage)
                .setCanGetMessage(canGetMessages)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllGroupChatMembers(
            GroupChat.GetAllGroupChatMembersRequest request,
            StreamObserver<GroupChat.GetAllGroupChatMembersResponse> responseObserver
    ) {
        Set<Long> memberIds = groupService.getAllMembers(request.getChatId());

        var response = GroupChat.GetAllGroupChatMembersResponse.newBuilder()
                .addAllUserId(memberIds)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
