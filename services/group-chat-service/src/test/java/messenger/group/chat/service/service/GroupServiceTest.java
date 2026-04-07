package messenger.group.chat.service.service;

import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.domain.repository.GroupRepository;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.ChangeGroupInfoRequest;
import messenger.group.chat.service.dto.request.CreateGroupRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.dto.response.GroupResponse;
import messenger.group.chat.service.exception.GroupNotFoundException;
import messenger.group.chat.service.validator.GroupPermissionValidator;
import messenger.group.chat.service.validator.GroupValidator;
import messenger.group.chat.service.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberService groupMemberService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private GroupValidator groupValidator;

    @Mock
    private GroupPermissionValidator groupPermissionValidator;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void createGroupChat_success() {
        Long creatorId = 1L;

        CreateGroupRequest request = new CreateGroupRequest(
                "group",
                "desc",
                List.of(2L, 3L),
                10L
        );

        GroupChat group = GroupChat.builder()
                .id(10L)
                .createdBy(creatorId)
                .name("group")
                .description("desc")
                .avatarId(10L)
                .build();

        when(groupRepository.save(any())).thenReturn(group);

        GroupResponse response = groupService.createGroupChat(creatorId, request);

        verify(groupValidator).validateCreatorNotInMembers(creatorId, request.userIds());
        verify(userValidator).validateUsersExist(request.userIds());
        verify(groupMemberService).addOwner(any(), eq(creatorId));
        verify(groupMemberService).addMembers(any(), eq(request.userIds()));

        assertEquals("group", response.getName());
    }

    @Test
    void deleteGroup_success() {
        Long removerId = 1L;
        Long groupId = 10L;

        GroupChat group = mock(GroupChat.class);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        groupService.deleteGroup(removerId, groupId);

        verify(groupPermissionValidator).validateCanDeleteGroup(removerId, groupId);
        verify(groupRepository).delete(group);
    }

    @Test
    void addNewMembers_success() {
        Long groupId = 10L;
        Long invitor = 1L;

        AddNewMembersRequest request = new AddNewMembersRequest(List.of(2L, 3L));

        GroupChat group = mock(GroupChat.class);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMemberService.addMembers(group, request.userIds())).thenReturn(List.of());

        List<GroupMemberResponse> result =
                groupService.addNewMembers(invitor, groupId, request);

        verify(groupPermissionValidator).validateCanAddMembers(invitor, groupId);
        verify(groupValidator).validateMembersNotAlreadyInGroup(groupId, request.userIds());
        verify(userValidator).validateUsersExist(request.userIds());
        verify(groupMemberService).addMembers(group, request.userIds());

        assertNotNull(result);
    }

    @Test
    void removeMembers_success() {
        Long groupId = 10L;
        Long remover = 1L;

        RemoveMembersRequest request = new RemoveMembersRequest(List.of(2L));

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(mock(GroupChat.class)));

        groupService.removeMembers(remover, groupId, request);

        verify(groupPermissionValidator)
                .validateCanRemoveMembers(remover, groupId, request.userIds());

        verify(groupMemberService)
                .removeMembers(groupId, request.userIds());
    }

    @Test
    void getGroupMembers_success() {
        Long groupId = 10L;
        Long userId = 1L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(mock(GroupChat.class)));
        when(groupMemberService.getGroupMembers(eq(groupId), any()))
                .thenReturn(List.of());

        List<GroupMemberResponse> result =
                groupService.getGroupMembers(userId, groupId, PageRequest.of(0,10));

        verify(groupPermissionValidator).validateCanGetGroupMembers(userId, groupId);
        assertNotNull(result);
    }

    @Test
    void changeGroupInfo_success() {
        Long groupId = 10L;
        Long changer = 1L;

        ChangeGroupInfoRequest request = new ChangeGroupInfoRequest("name",10L,"avatar");

        GroupChat group = mock(GroupChat.class);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepository.save(group)).thenReturn(group);

        GroupResponse response =
                groupService.changeGroupInfo(changer, groupId, request);

        verify(groupPermissionValidator).validateCanChangeGroupInfo(changer, groupId);
        verify(group).changeFrom(request);
        verify(groupRepository).save(group);

        assertNotNull(response);
    }

    @Test
    void findGroupById_notFound() {
        Long groupId = 10L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class,
                () -> groupService.findGroupById(groupId));
    }

    @Test
    void getAllMembers_success() {
        Long groupId = 10L;

        when(groupMemberRepository.findAllUserIdsByGroupId(groupId))
                .thenReturn(Set.of(1L,2L));

        Set<Long> result = groupService.getAllMembers(groupId);

        assertEquals(2, result.size());
    }

    @Test
    void updateLastActivity_success() {
        Long groupId = 10L;
        Instant now = Instant.now();

        groupService.updateLastActivity(groupId, now);

        verify(groupRepository).updateLastActivity(groupId, now);
    }
}