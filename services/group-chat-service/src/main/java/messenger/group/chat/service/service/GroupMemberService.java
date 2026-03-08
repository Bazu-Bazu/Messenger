package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.dto.UserInfoDto;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupCacheService cacheEvictionService;
    private final UserGrpcClient userGrpcClient;

    @Transactional
    public void addOwner(GroupChat group, Long ownerId) {
        group.addMember(createGroupMember(ownerId, GroupMemberRole.OWNER));

        cacheEvictionService.evictUsersChats(List.of(ownerId));
    }

    @Transactional
    public List<GroupMemberResponse> addMembers(GroupChat group, List<Long> userIds) {
        List<GroupChatMember> newMembers = userIds.stream()
                .map(userId -> (createGroupMember(userId, GroupMemberRole.MEMBER)))
                .toList();

        group.addMembers(newMembers);

        cacheEvictionService.evictUsersChats(userIds);

        List<GroupChatMember> savedMembers = groupMemberRepository.saveAll(newMembers);
        return createGroupMemberResponses(savedMembers);
    }

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> getGroupMembers(Long groupId, Pageable pageable) {
        List<GroupChatMember> members = groupMemberRepository.findAllByGroupId(groupId, pageable);

        return createGroupMemberResponses(members);
    }

    @Transactional
    public void removeMembers(Long groupId, List<Long> userIds) {
        groupMemberRepository.deleteByUserIdsAndGroupId(userIds, groupId);

        cacheEvictionService.evictUsersChats(userIds);
    }

    @Transactional
    public List<GroupMemberResponse> setRoles(Long groupId, List<Long> userIds, GroupMemberRole role) {
        groupMemberRepository.setRoleByUserIdsAndGroupId(role, userIds, groupId);

        List<GroupChatMember> changedMembers = groupMemberRepository.findAllByUserIdsAndGroupId(userIds, groupId);
        return createGroupMemberResponses(changedMembers);
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

        List<UserInfoDto> responses = userGrpcClient.getUsersInfo(userIds);

        Map<Long, UserInfoDto> userInfoMap = responses.stream()
                .collect(Collectors.toMap(UserInfoDto::userId, Function.identity()));

        return groupMembers.stream()
                .map(member -> createGroupMemberResponse(member, userInfoMap.get(member.getUserId())))
                .toList();
    }

    private GroupMemberResponse createGroupMemberResponse(GroupChatMember groupMember, UserInfoDto userInfo) {
        return GroupMemberResponse.builder()
                .id(groupMember.getId())
                .role(groupMember.getRole())
                .userInfo(userInfo)
                .build();
    }
}
