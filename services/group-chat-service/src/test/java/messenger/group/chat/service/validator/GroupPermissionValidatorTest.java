package messenger.group.chat.service.validator;

import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.exception.AuthorizationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupPermissionValidatorTest {

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private GroupChatMember manager;

    @Mock
    private GroupChatMember member;

    @InjectMocks
    private GroupPermissionValidator validator;

    private final Long userId = 1L;
    private final Long groupId = 10L;

    @Test
    void validateCanAddMembers_success() {
        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));
        when(manager.canInviteMembers()).thenReturn(true);

        assertDoesNotThrow(() ->
                validator.validateCanAddMembers(userId, groupId)
        );
    }

    @Test
    void validateCanAddMembers_noPermission() {
        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));
        when(manager.canInviteMembers()).thenReturn(false);

        assertThrows(AuthorizationException.class,
                () -> validator.validateCanAddMembers(userId, groupId));
    }

    @Test
    void validateCanGetGroupMembers_userNotInGroup() {
        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.empty());

        assertThrows(AuthorizationException.class,
                () -> validator.validateCanGetGroupMembers(userId, groupId));
    }

    @Test
    void validateCanRemoveMembers_success() {
        List<Long> ids = List.of(2L);

        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));

        when(groupMemberRepository.findAllByUserIdsAndGroupId(ids, groupId))
                .thenReturn(List.of(member));

        when(manager.canRemoveMembers()).thenReturn(true);
        when(manager.canManage(any())).thenReturn(true);

        assertDoesNotThrow(() ->
                validator.validateCanRemoveMembers(userId, groupId, ids));
    }

    @Test
    void validateCanRemoveMembers_noPermission() {
        List<Long> ids = List.of(2L);

        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));
        when(manager.canRemoveMembers()).thenReturn(false);

        assertThrows(AuthorizationException.class,
                () -> validator.validateCanRemoveMembers(userId, groupId, ids));
    }

    @Test
    void validateCanSetRole_success() {
        List<Long> ids = List.of(2L);

        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));

        when(groupMemberRepository.findAllByUserIdsAndGroupId(ids, groupId))
                .thenReturn(List.of(member));

        when(manager.canSetRole(GroupMemberRole.MEMBER)).thenReturn(true);
        when(manager.canManage(any())).thenReturn(true);

        assertDoesNotThrow(() ->
                validator.validateCanSetRole(userId, groupId, ids, GroupMemberRole.MEMBER));
    }

    @Test
    void validateCanDeleteGroup_noPermission() {
        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));
        when(manager.canDeleteGroup()).thenReturn(false);

        assertThrows(AuthorizationException.class,
                () -> validator.validateCanDeleteGroup(userId, groupId));
    }

    @Test
    void validateCanChangeGroupInfo_success() {
        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));
        when(manager.canChangeGroupInfo()).thenReturn(true);

        assertDoesNotThrow(() ->
                validator.validateCanChangeGroupInfo(userId, groupId));
    }

    @Test
    void validateCanManage_memberHigherRole() {
        List<Long> ids = List.of(2L);

        when(groupMemberRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(manager));

        when(groupMemberRepository.findAllByUserIdsAndGroupId(ids, groupId))
                .thenReturn(List.of(member));

        when(manager.canRemoveMembers()).thenReturn(true);
        when(manager.canManage(any())).thenReturn(false);

        when(member.getRole()).thenReturn(GroupMemberRole.ADMIN);
        when(member.getUserId()).thenReturn(2L);
        when(manager.getUserId()).thenReturn(userId);

        assertThrows(AuthorizationException.class,
                () -> validator.validateCanRemoveMembers(userId, groupId, ids));
    }
}
