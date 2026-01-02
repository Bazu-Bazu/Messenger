package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.repository.GroupChatRepository;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.CreateGroupChatRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.exception.GroupChatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final UserGrpcClient userGrpcClient;
    private final MemberAdditionService memberAdditionService;
    private final ValidationMemberRightsService validationMemberRightsService;

    @Transactional
    public GroupChatResponse createGroupChat(Long creatorId, CreateGroupChatRequest request) {
        GroupChat newGroup = GroupChat.builder()
                .createdBy(creatorId)
                .name(request.name())
                .description(request.description())
                .avatarUrl(request.avatarUrl())
                .createdBy(creatorId)
                .build();

        GroupChat savedGroup = groupChatRepository.save(newGroup);

        memberAdditionService.addOwnerToGroup(savedGroup, creatorId);
        memberAdditionService.addMembersToNewGroup(savedGroup, request.userIds());

        return createGroupChatResponse(savedGroup);
    }

    @Transactional
    public void addNewMembers(Long invitorId, AddNewMembersRequest request) {
        userGrpcClient.validateUsersExist(List.of(invitorId));

        Long groupId = request.groupId();
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new GroupChatException(
                        String.format("Group with id %d not found", groupId)
                ));

        validationMemberRightsService.validateCanAddMembers(groupId, invitorId);

        memberAdditionService.addMembersToGroup(group, request.userIds());
    }

    @Transactional
    public void removeMembers(Long removerId, RemoveMembersRequest request) {
        userGrpcClient.validateUsersExist(List.of(removerId));

        Long groupId = request.groupId();
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new GroupChatException(
                        String.format("Group with id %d not found", groupId)
                ));

        validationMemberRightsService.validateCanRemoveMembers(groupId, removerId, request.userIds());

        memberAdditionService.removeMembersFromGroup(group, request.userIds());
    }

    @Transactional
    public void deleteGroup(Long ownerId, Long groupId) {
        userGrpcClient.validateUsersExist(List.of(ownerId));

        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new GroupChatException(
                        String.format("Group with id %d not found", groupId)
                ));

        validationMemberRightsService.validateCanDeleteGroup(group, ownerId);

        groupChatRepository.delete(group);
    }

    public List<GroupMemberResponse> getGroupMembers(Long userId, Long groupId) {
        userGrpcClient.validateUsersExist(List.of(userId));

        validationMemberRightsService.validateCanGetGroupMembers(groupId, userId);

        return memberAdditionService.getGroupMembers(groupId);
    }

    private GroupChatResponse createGroupChatResponse(GroupChat group) {
        return GroupChatResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .createdBy(group.getCreatedBy())
                .createdAt(group.getCreatedAt())
                .lastActivityAt(group.getLastActivityAt())
                .avatarUrl(group.getAvatarUrl())
                .build();
    }

}
