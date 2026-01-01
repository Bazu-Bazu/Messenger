package messenger.group.chat.service.service;

import exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupChatMemberRepository;
import messenger.group.chat.service.exception.GroupChatMemberException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationMemberRightsService {

    private final GroupChatMemberRepository groupChatMemberRepository;

    public void validateCanAddMembers(Long groupId, Long invitorId) {
        GroupChatMember member = groupChatMemberRepository.findByGroupIdAndUserId(groupId, invitorId)
                .orElseThrow(() -> new GroupChatMemberException(
                        String.format("Group %d member with id %d not found", groupId, invitorId)
                ));

        if (!member.canInviteMembers()) {
            throw new AuthorizationException(
                    String.format("User %d cannot invite members to group %d", invitorId, groupId)
            );
        }
    }

    public void validateCanRemoveMembers(Long groupId, Long removerId, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return;

        GroupChatMember member = groupChatMemberRepository.findByGroupIdAndUserId(groupId, removerId)
                .orElseThrow(() -> new GroupChatMemberException(
                        String.format("Group %d member with id %d not found", groupId, removerId)
                ));

        if (!member.canRemoveMembers()) {
            throw new AuthorizationException(
                    String.format("User %d cannot remove members from group %d", removerId, groupId)
            );
        }

        validateCanManageUsers(groupId, member, userIds);
    }

    private void validateCanManageUsers(Long groupId, GroupChatMember member, List<Long> userIds) {
        Set<GroupMemberRole> roles = groupChatMemberRepository.findRolesByUserIdsAndGroupId(userIds, groupId);
        roles.forEach(role -> {
            if (!member.getRole().canManage(role)) {
                throw new AuthorizationException(
                        String.format("User %d cannot manage user with %s", member.getUserId(), role)
                );
            }
        });
    }

    public void validateCanDeleteGroup(GroupChat group, Long userId) {
        if (!group.getCreatedBy().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d is not the owner of group %d", userId, group.getId())
            );
        }
    }

}
