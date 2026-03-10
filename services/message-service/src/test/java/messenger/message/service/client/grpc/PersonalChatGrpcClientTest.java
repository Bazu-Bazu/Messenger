package messenger.message.service.client.grpc;

import messenger.message.service.dto.MemberRightsInPersonalChatDto;
import messenger.message.service.mapper.PersonalChatGrpcMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import personal_chat.PersonalChat;
import personal_chat.PersonalChatServiceGrpc;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalChatGrpcClientTest {

    @Mock
    private PersonalChatServiceGrpc.PersonalChatServiceBlockingStub blockingStub;

    @Mock
    private PersonalChatGrpcMapper personalChatGrpcMapper;

    @InjectMocks
    private PersonalChatGrpcClient grpcClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(grpcClient, "blockingStub", blockingStub);
    }

    @Test
    void getMemberRightsInPersonalChat_shouldCallStubAndMapResponse() {
        Long userId = 1L;
        Long chatId = 10L;

        PersonalChat.ValidateUserIsMemberOfPersonalChatResponse grpcResponse =
                mock(PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.class);
        MemberRightsInPersonalChatDto dto = mock(MemberRightsInPersonalChatDto.class);

        when(blockingStub.validateUserIsMemberOfPersonalChat(any())).thenReturn(grpcResponse);
        when(personalChatGrpcMapper.fromGrpc(grpcResponse)).thenReturn(dto);

        MemberRightsInPersonalChatDto result = grpcClient.getMemberRightsInPersonalChat(userId, chatId);

        ArgumentCaptor<PersonalChat.ValidateUserIsMemberOfPersonalChatRequest> captor =
                ArgumentCaptor.forClass(PersonalChat.ValidateUserIsMemberOfPersonalChatRequest.class);
        verify(blockingStub).validateUserIsMemberOfPersonalChat(captor.capture());
        verify(personalChatGrpcMapper).fromGrpc(grpcResponse);

        PersonalChat.ValidateUserIsMemberOfPersonalChatRequest requestSent = captor.getValue();
        assertEquals(userId, requestSent.getUserId());
        assertEquals(chatId, requestSent.getChatId());

        assertEquals(dto, result);
    }

    @Test
    void getAllPersonalChatMembers_shouldCallStubAndReturnSet() {
        Long chatId = 20L;

        PersonalChat.GetAllPersonalChatMembersResponse grpcResponse =
                mock(PersonalChat.GetAllPersonalChatMembersResponse.class);

        when(grpcResponse.getUserIdList()).thenReturn(List.of(1L, 2L, 3L));
        when(blockingStub.getAllPersonalChatMembers(any())).thenReturn(grpcResponse);

        Set<Long> result = grpcClient.getAllPersonalChatMembers(chatId);

        ArgumentCaptor<PersonalChat.GetAllPersonalChatMembersRequest> captor =
                ArgumentCaptor.forClass(PersonalChat.GetAllPersonalChatMembersRequest.class);
        verify(blockingStub).getAllPersonalChatMembers(captor.capture());

        PersonalChat.GetAllPersonalChatMembersRequest requestSent = captor.getValue();
        assertEquals(chatId, requestSent.getChatId());

        assertEquals(Set.of(1L, 2L, 3L), result);
    }
}
