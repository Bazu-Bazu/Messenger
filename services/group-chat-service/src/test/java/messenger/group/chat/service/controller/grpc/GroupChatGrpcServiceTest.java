package messenger.group.chat.service.controller.grpc;

import group_chat.GroupChat;
import io.grpc.stub.StreamObserver;
import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupChatGrpcServiceTest {

    @Mock
    private GroupService groupService;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private StreamObserver<GroupChat.ValidateMemberRightsInGroupChatResponse> rightsObserver;

    @Mock
    private StreamObserver<GroupChat.GetAllGroupChatMembersResponse> membersObserver;

    @InjectMocks
    private GroupChatGrpcService grpcService;

    @Test
    void validateMemberRights_memberExistsAndCanSend() {
        Long chatId = 10L;
        Long userId = 5L;

        GroupChatMember member = mock(GroupChatMember.class);
        when(member.canSendMessage()).thenReturn(true);

        when(groupMemberRepository.findByGroupIdAndUserId(chatId, userId))
                .thenReturn(Optional.of(member));

        GroupChat.ValidateMemberRightsInGroupChatRequest request =
                GroupChat.ValidateMemberRightsInGroupChatRequest.newBuilder()
                        .setChatId(chatId)
                        .setUserId(userId)
                        .build();

        grpcService.validateMemberRightsInGroupChat(request, rightsObserver);

        ArgumentCaptor<GroupChat.ValidateMemberRightsInGroupChatResponse> captor =
                ArgumentCaptor.forClass(GroupChat.ValidateMemberRightsInGroupChatResponse.class);

        verify(rightsObserver).onNext(captor.capture());
        verify(rightsObserver).onCompleted();

        var response = captor.getValue();

        assertTrue(response.getCanSendMessage());
        assertTrue(response.getCanGetMessage());
        assertEquals(userId, response.getUserId());
        assertEquals(chatId, response.getChatId());
    }

    @Test
    void validateMemberRights_memberNotExists() {
        Long chatId = 10L;
        Long userId = 5L;

        when(groupMemberRepository.findByGroupIdAndUserId(chatId, userId))
                .thenReturn(Optional.empty());

        GroupChat.ValidateMemberRightsInGroupChatRequest request =
                GroupChat.ValidateMemberRightsInGroupChatRequest.newBuilder()
                        .setChatId(chatId)
                        .setUserId(userId)
                        .build();

        grpcService.validateMemberRightsInGroupChat(request, rightsObserver);

        ArgumentCaptor<GroupChat.ValidateMemberRightsInGroupChatResponse> captor =
                ArgumentCaptor.forClass(GroupChat.ValidateMemberRightsInGroupChatResponse.class);

        verify(rightsObserver).onNext(captor.capture());
        verify(rightsObserver).onCompleted();

        var response = captor.getValue();

        assertFalse(response.getCanSendMessage());
        assertFalse(response.getCanGetMessage());
    }

    @Test
    void getAllGroupChatMembers_success() {
        Long chatId = 10L;

        when(groupService.getAllMembers(chatId))
                .thenReturn(Set.of(1L, 2L, 3L));

        GroupChat.GetAllGroupChatMembersRequest request =
                GroupChat.GetAllGroupChatMembersRequest.newBuilder()
                        .setChatId(chatId)
                        .build();

        grpcService.getAllGroupChatMembers(request, membersObserver);

        ArgumentCaptor<GroupChat.GetAllGroupChatMembersResponse> captor =
                ArgumentCaptor.forClass(GroupChat.GetAllGroupChatMembersResponse.class);

        verify(membersObserver).onNext(captor.capture());
        verify(membersObserver).onCompleted();

        var response = captor.getValue();

        assertEquals(3, response.getUserIdCount());
        assertTrue(response.getUserIdList().contains(1L));
    }
}
