package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.repository.GroupChatMemberRepository;
import messenger.group.chat.service.domain.repository.GroupChatRepository;
import messenger.group.chat.service.dto.request.*;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.exception.GroupChatException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final GroupChatMemberRepository groupChatMemberRepository;
    private final UserGrpcClient userGrpcClient;
    private final MemberAdditionService memberAdditionService;
    private final ValidationMemberRightsService validationMemberRightsService;
    private final CacheEvictionService cacheEvictionService;

    @Transactional
    public GroupChatResponse createGroupChat(Long creatorId, CreateGroupChatRequest request) {
        GroupChat newGroup = GroupChat.builder()
                .createdBy(creatorId)
                .name(request.name())
                .description(request.description())
                .avatarUrl(request.avatarUrl())
                .build();

        GroupChat savedGroup = groupChatRepository.save(newGroup);

        memberAdditionService.addOwnerToGroup(savedGroup, creatorId);
        memberAdditionService.addMembersToNewGroup(savedGroup, request.userIds());

        return createGroupChatResponse(savedGroup);
    }

    @Transactional
    public void addNewMembers(Long invitorId, Long groupId, AddNewMembersRequest request) {
        userGrpcClient.validateUsersExist(List.of(invitorId));

        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new GroupChatException(
                        String.format("Group with id %d not found", groupId)
                ));

        validationMemberRightsService.validateCanAddMembers(groupId, invitorId);

        memberAdditionService.addMembersToGroup(group, request.userIds());
    }

    @Transactional
    public void removeMembers(Long removerId, Long groupId, RemoveMembersRequest request) {
        userGrpcClient.validateUsersExist(List.of(removerId));

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

        cacheEvictionService.evictGroupChatCache(groupId);
        cacheEvictionService.evictGroupMembersCache(groupId);
    }

    public List<GroupMemberResponse> getGroupMembers(Long userId, Long groupId) {
        userGrpcClient.validateUsersExist(List.of(userId));

        validationMemberRightsService.validateCanGetGroupMembers(groupId, userId);

        return memberAdditionService.getGroupMembers(groupId);
    }

    @Transactional
    public GroupChatResponse changeGroupInfo(Long changerId, Long groupId, ChangeGroupInfoRequest request) {
        userGrpcClient.validateUsersExist(List.of(changerId));

        validationMemberRightsService.validateCanChangeGroupInfo(groupId, changerId);

        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new GroupChatException(
                        String.format("Group with id %d not found", groupId)
                ));

        group.changeFrom(request);
        GroupChat savedGroup = groupChatRepository.save(group);

        Set<Long> memberIds = groupChatMemberRepository.findAllUserIdsByGroupId(groupId);
        memberIds.forEach(cacheEvictionService::evictUserGroupChats);
        cacheEvictionService.evictGroupChatCache(groupId);

        return createGroupChatResponse(savedGroup);
    }

    @Cacheable(value = CacheEvictionService.USER_GROUP_CHATS_CACHE, key = "#userId")
    public List<GroupChatResponse> getAllUserGroupChat(Long userId) {
        userGrpcClient.validateUsersExist(List.of(userId));

        List<GroupChat> groupIds = groupChatRepository.findAllUserChatIds(userId);

        return groupIds.stream()
                .map(this::createGroupChatResponse)
                .toList();
    }

    @Transactional
    public void updateLastActivity(Long groupId, Instant lastActivity) {
        groupChatRepository.updateLastActivity(groupId, lastActivity);
    }

    public List<GroupMemberResponse> setRoles(Long setterId, Long groupId, SetRolesRequest request) {
        userGrpcClient.validateUsersExist(List.of(setterId));

        validationMemberRightsService.validateCanSetRole(groupId, setterId, request.userIds(), request.role());

        return memberAdditionService.setRoles(groupId, request.userIds(), request.role());
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
