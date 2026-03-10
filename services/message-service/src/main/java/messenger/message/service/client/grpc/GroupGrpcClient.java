package messenger.message.service.client.grpc;

import group_chat.GroupChat;
import group_chat.GroupChatServiceGrpc;
import lombok.RequiredArgsConstructor;
import messenger.message.service.dto.MemberRightsInGroupDto;
import messenger.message.service.mapper.GroupGrpcMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupGrpcClient {

    @GrpcClient("group-chat-service")
    private GroupChatServiceGrpc.GroupChatServiceBlockingStub blockingStub;

    private final GroupGrpcMapper groupGrpcMapper;

    public MemberRightsInGroupDto getMemberRightsInGroup(Long userId, Long chatId) {
        var request = GroupChat.ValidateMemberRightsInGroupChatRequest.newBuilder()
                .setUserId(userId)
                .setChatId(chatId)
                .build();

        var response = blockingStub.validateMemberRightsInGroupChat(request);
        return groupGrpcMapper.fromGrpc(response);
    }

    public Set<Long> getAllGroupChatMembers(Long chatId) {
        var request = GroupChat.GetAllGroupChatMembersRequest.newBuilder()
                .setChatId(chatId)
                .build();

        var response = blockingStub.getAllGroupChatMembers(request);

        return new HashSet<>(response.getUserIdList());
    }
}
