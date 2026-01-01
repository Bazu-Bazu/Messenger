package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupChatMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class MemberAdditionService {

    private final GroupChatMemberRepository groupChatMemberRepository;
    private final UserGrpcClient userGrpcClient;

    public void addOwnerToGroup(GroupChat group, Long ownerId) {
        userGrpcClient.validateUsersExist(List.of(ownerId));

        group.addMember(createGroupMember(ownerId, GroupMemberRole.OWNER));
    }

    public void addMembersToNewGroup(GroupChat group, List<Long> userIds) {
        List<Long> usersToAdd = removeOwnerAndDuplicates(group.getCreatedBy(), userIds);

        userGrpcClient.validateUsersExist(usersToAdd);

        List<GroupChatMember> newMembers = usersToAdd.stream()
                .map(userId -> (createGroupMember(userId, GroupMemberRole.MEMBER)))
                .toList();

        group.addMembers(newMembers);
    }

    private GroupChatMember createGroupMember(Long userId, GroupMemberRole role) {
        return GroupChatMember.builder()
                .userId(userId)
                .role(role)
                .build();
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

}
