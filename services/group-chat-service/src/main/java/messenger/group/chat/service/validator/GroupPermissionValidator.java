package messenger.group.chat.service.validator;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.exception.AuthorizationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupPermissionValidator {

    private final GroupMemberRepository groupMemberRepository;

    public void validateCanAddMembers(Long userId, Long groupId) {
        GroupChatMember member = findByGroupIdAndUserId(userId, groupId);

        if (!member.canInviteMembers()) {
            throw new AuthorizationException(
                    String.format("User %d cannot invite members to group %d", userId, groupId)
            );
        }
    }

    public void validateCanGetGroupMembers(Long userId, Long groupId) {
        findByGroupIdAndUserId(userId, groupId);
    }

    public void validateCanRemoveMembers(Long userId, Long groupId, List<Long> userIds) {
        GroupChatMember remover = findByGroupIdAndUserId(userId, groupId);

        List<GroupChatMember> groupMembers = groupMemberRepository.findAllByUserIdsAndGroupId(userIds, groupId);

        if (!remover.canRemoveMembers()) {
            throw new AuthorizationException(
                    String.format("User %d cannot remove members from group %d", userId, groupId)
            );
        }

        validateCanManage(remover, groupMembers, groupId);
    }

    public void validateCanSetRole(Long userId, Long groupId, List<Long> userIds, GroupMemberRole role) {
        GroupChatMember setter = findByGroupIdAndUserId(userId, groupId);

        List<GroupChatMember> groupMembers = groupMemberRepository.findAllByUserIdsAndGroupId(userIds, groupId);

        if (!setter.canSetRole(role)) {
            throw new AuthorizationException(
                    String.format("User %d cannot set role %s in group %d", userId, role, groupId)
            );
        }

        validateCanManage(setter, groupMembers, groupId);
    }

    public void validateCanDeleteGroup(Long userId, Long groupId) {
        GroupChatMember remover = findByGroupIdAndUserId(userId, groupId);

        if (!remover.canDeleteGroup()) {
            throw new AuthorizationException(
                    String.format("User %d cannot delete group %d", userId, groupId)
            );
        }
    }

    public void validateCanChangeGroupInfo(Long userId, Long groupId) {
        GroupChatMember remover = findByGroupIdAndUserId(userId, groupId);

        if (!remover.canChangeGroupInfo()) {
            throw new AuthorizationException(
                    String.format("User %d cannot change info in group %d", userId, groupId)
            );
        }
    }

    private GroupChatMember findByGroupIdAndUserId(Long userId, Long groupId) {
        return groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new AuthorizationException(
                        String.format("User %d is not a member of the group %d", userId, groupId)
                ));
    }

    private void validateCanManage(GroupChatMember manager, List<GroupChatMember> members, Long groupId) {
        members.forEach(member -> {
            if (!manager.canManage(member.getRole())) {
                throw new AuthorizationException(
                        String.format("User %d cannot manage member %d in group %d",
                                manager.getUserId(), member.getUserId(), groupId)
                );
            }
        });
    }
}
