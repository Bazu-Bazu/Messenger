package messenger.message.service.client.grpc;

import group_chat.GroupChat;
import group_chat.GroupChatServiceGrpc;
import messenger.message.service.dto.MemberRightsInGroupDto;
import messenger.message.service.mapper.GroupGrpcMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupGrpcClientTest {

    @Mock
    private GroupChatServiceGrpc.GroupChatServiceBlockingStub blockingStub;

    @Mock
    private GroupGrpcMapper groupGrpcMapper;

    @InjectMocks
    private GroupGrpcClient grpcClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(grpcClient, "blockingStub", blockingStub);
    }

    @Test
    void getMemberRightsInGroup_shouldCallStubAndMapResponse() {
        Long userId = 1L;
        Long chatId = 10L;

        GroupChat.ValidateMemberRightsInGroupChatResponse grpcResponse =
                mock(GroupChat.ValidateMemberRightsInGroupChatResponse.class);
        MemberRightsInGroupDto dto = mock(MemberRightsInGroupDto.class);

        when(blockingStub.validateMemberRightsInGroupChat(any())).thenReturn(grpcResponse);
        when(groupGrpcMapper.fromGrpc(grpcResponse)).thenReturn(dto);

        MemberRightsInGroupDto result = grpcClient.getMemberRightsInGroup(userId, chatId);

        ArgumentCaptor<GroupChat.ValidateMemberRightsInGroupChatRequest> captor =
                ArgumentCaptor.forClass(GroupChat.ValidateMemberRightsInGroupChatRequest.class);
        verify(blockingStub).validateMemberRightsInGroupChat(captor.capture());
        verify(groupGrpcMapper).fromGrpc(grpcResponse);

        GroupChat.ValidateMemberRightsInGroupChatRequest requestSent = captor.getValue();
        assertEquals(userId, requestSent.getUserId());
        assertEquals(chatId, requestSent.getChatId());

        assertEquals(dto, result);
    }

    @Test
    void getAllGroupChatMembers_shouldCallStubAndReturnSet() {
        Long chatId = 20L;

        GroupChat.GetAllGroupChatMembersResponse grpcResponse =
                mock(GroupChat.GetAllGroupChatMembersResponse.class);

        when(grpcResponse.getUserIdList()).thenReturn(List.of(1L, 2L, 3L));
        when(blockingStub.getAllGroupChatMembers(any())).thenReturn(grpcResponse);

        Set<Long> result = grpcClient.getAllGroupChatMembers(chatId);

        ArgumentCaptor<GroupChat.GetAllGroupChatMembersRequest> captor =
                ArgumentCaptor.forClass(GroupChat.GetAllGroupChatMembersRequest.class);
        verify(blockingStub).getAllGroupChatMembers(captor.capture());

        GroupChat.GetAllGroupChatMembersRequest requestSent = captor.getValue();
        assertEquals(chatId, requestSent.getChatId());

        assertEquals(Set.of(1L, 2L, 3L), result);
    }
}
