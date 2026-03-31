package messenger.group.chat.service.service;

import messenger.group.chat.service.client.grpc.UserGrpcClient;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.dto.UserInfoDto;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupMemberServiceTest {

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private GroupCacheService cacheEvictionService;

    @Mock
    private UserGrpcClient userGrpcClient;

    @InjectMocks
    private GroupMemberService groupMemberService;

    @Test
    void addOwner_success() {
        GroupChat group = mock(GroupChat.class);
        Long ownerId = 1L;

        groupMemberService.addOwner(group, ownerId);

        verify(group).addMember(any(GroupChatMember.class));
        verify(cacheEvictionService).evictUsersChats(List.of(ownerId));
    }

    @Test
    void addMembers_success() {
        GroupChat group = mock(GroupChat.class);

        List<Long> userIds = List.of(2L, 3L);

        GroupChatMember m1 = GroupChatMember.builder().id(1L).userId(2L).role(GroupMemberRole.MEMBER).build();
        GroupChatMember m2 = GroupChatMember.builder().id(2L).userId(3L).role(GroupMemberRole.MEMBER).build();

        List<GroupChatMember> savedMembers = List.of(m1, m2);

        when(groupMemberRepository.saveAll(any())).thenReturn(savedMembers);

        when(userGrpcClient.getUsersInfo(userIds))
                .thenReturn(List.of(
                        new UserInfoDto(2L, "user2", 10L),
                        new UserInfoDto(3L, "user3", 10L)
                ));

        List<GroupMemberResponse> result =
                groupMemberService.addMembers(group, userIds);

        verify(group).addMembers(any());
        verify(cacheEvictionService).evictUsersChats(userIds);
        verify(groupMemberRepository).saveAll(any());

        assertEquals(2, result.size());
    }

    @Test
    void getGroupMembers_success() {
        Long groupId = 10L;

        GroupChatMember member = GroupChatMember.builder()
                .id(1L)
                .userId(2L)
                .role(GroupMemberRole.MEMBER)
                .build();

        when(groupMemberRepository.findAllByGroupId(eq(groupId), any()))
                .thenReturn(List.of(member));

        when(userGrpcClient.getUsersInfo(List.of(2L)))
                .thenReturn(List.of(new UserInfoDto(2L, "user2", 10L)));

        List<GroupMemberResponse> result =
                groupMemberService.getGroupMembers(groupId, PageRequest.of(0, 10));

        assertEquals(1, result.size());
        assertEquals(GroupMemberRole.MEMBER, result.get(0).getRole());
    }

    @Test
    void removeMembers_success() {
        Long groupId = 10L;
        List<Long> userIds = List.of(2L, 3L);

        groupMemberService.removeMembers(groupId, userIds);

        verify(groupMemberRepository).deleteByUserIdsAndGroupId(userIds, groupId);
        verify(cacheEvictionService).evictUsersChats(userIds);
    }

    @Test
    void setRoles_success() {
        Long groupId = 10L;
        List<Long> userIds = List.of(2L);

        GroupChatMember member = GroupChatMember.builder()
                .id(1L)
                .userId(2L)
                .role(GroupMemberRole.ADMIN)
                .build();

        when(groupMemberRepository.findAllByUserIdsAndGroupId(userIds, groupId))
                .thenReturn(List.of(member));

        when(userGrpcClient.getUsersInfo(userIds))
                .thenReturn(List.of(new UserInfoDto(2L, "user2", 10L)));

        List<GroupMemberResponse> result =
                groupMemberService.setRoles(groupId, userIds, GroupMemberRole.ADMIN);

        verify(groupMemberRepository)
                .setRoleByUserIdsAndGroupId(GroupMemberRole.ADMIN, userIds, groupId);

        assertEquals(1, result.size());
        assertEquals(GroupMemberRole.ADMIN, result.get(0).getRole());
    }
}
