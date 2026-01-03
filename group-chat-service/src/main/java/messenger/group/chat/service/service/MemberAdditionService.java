package messenger.group.chat.service.service;

import dto.response.UserInfo;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupChatMemberRepository;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberAdditionService {

    private final GroupChatMemberRepository groupChatMemberRepository;
    private final UserGrpcClient userGrpcClient;
    private final CacheEvictionService cacheEvictionService;

    @Transactional
    public void addOwnerToGroup(GroupChat group, Long ownerId) {
        userGrpcClient.validateUsersExist(List.of(ownerId));

        group.addMember(createGroupMember(ownerId, GroupMemberRole.OWNER));

        cacheEvictionService.evictUserGroupChats(ownerId);
    }

    @Transactional
    public void addMembersToNewGroup(GroupChat group, List<Long> userIds) {
        List<Long> usersToAdd = removeOwnerAndDuplicates(group.getCreatedBy(), userIds);

        userGrpcClient.validateUsersExist(usersToAdd);

        List<GroupChatMember> newMembers = usersToAdd.stream()
                .map(userId -> (createGroupMember(userId, GroupMemberRole.MEMBER)))
                .toList();

        group.addMembers(newMembers);

        usersToAdd.forEach(cacheEvictionService::evictUserGroupChats);
    }

    @Transactional
    public void addMembersToGroup(GroupChat group, List<Long> userIds) {
        List<Long> usersToAdd = removeExistingMembersAndDuplicates(group.getId(), userIds);

        userGrpcClient.validateUsersExist(usersToAdd);

        List<GroupChatMember> newMembers = usersToAdd.stream()
                .map(userId -> (createGroupMember(userId, GroupMemberRole.MEMBER)))
                .toList();

        group.addMembers(newMembers);

        cacheEvictionService.evictGroupMembersCache(group.getId());
        usersToAdd.forEach(cacheEvictionService::evictUserGroupChats);
    }

    @Transactional
    public void removeMembersFromGroup(GroupChat group, List<Long> userIds) {
        List<Long> usersToDelete = removeDuplicates(userIds);

        groupChatMemberRepository.deleteByUserIdsAndGroupId(usersToDelete, group.getId());

        cacheEvictionService.evictGroupMembersCache(group.getId());
        usersToDelete.forEach(cacheEvictionService::evictUserGroupChats);
    }

    @Cacheable(value = CacheEvictionService.GROUP_MEMBERS_CACHE, key = "#groupId")
    public List<GroupMemberResponse> getGroupMembers(Long groupId) {
        List<GroupChatMember> groupMembers = groupChatMemberRepository.findAllByGroupId(groupId);

        return createGroupMemberResponses(groupMembers);
    }

    private GroupChatMember createGroupMember(Long userId, GroupMemberRole role) {
        return GroupChatMember.builder()
                .userId(userId)
                .role(role)
                .build();
    }

    private List<GroupMemberResponse> createGroupMemberResponses(List<GroupChatMember> groupMembers) {
        List<Long> userIds = groupMembers.stream()
                .map(GroupChatMember::getUserId)
                .toList();

         Map<Long, UserInfo> usersInfoMap = userGrpcClient.getUsersInfo(userIds);

         return groupMembers.stream()
                 .map(member -> GroupMemberResponse.builder()
                         .id(member.getId())
                         .customNickname(member.getCustomNickname())
                         .role(member.getRole())
                         .userInfo(usersInfoMap.get(member.getUserId()))
                         .build()
                 )
                 .toList();
    }

    private List<Long> removeOwnerAndDuplicates(Long ownerId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return List.of();

        return userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .filter(userId -> !Objects.equals(userId, ownerId))
                .toList();
    }

    private List<Long> removeExistingMembersAndDuplicates(Long groupId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return List.of();

        Set<Long> existingUserIdsInGroup = groupChatMemberRepository.findAllUserIdsByGroupId(groupId);

        return userIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .filter(userId -> !existingUserIdsInGroup.contains(userId))
                .toList();
    }

    private List<Long> removeDuplicates(List<Long> userIds) {
        return userIds.stream()
                .distinct()
                .toList();
    }

}
