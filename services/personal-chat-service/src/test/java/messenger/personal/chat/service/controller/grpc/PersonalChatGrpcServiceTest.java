package messenger.personal.chat.service.controller.grpc;

import io.grpc.stub.StreamObserver;
import messenger.personal.chat.service.service.PersonalChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import personal_chat.PersonalChat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalChatGrpcServiceTest {

    @Mock
    private PersonalChatService personalChatService;

    @Mock
    private StreamObserver<PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> validateObserver;

    @Mock
    private StreamObserver<PersonalChat.GetAllPersonalChatMembersResponse> membersObserver;

    @InjectMocks
    private PersonalChatGrpcService grpcService;

    @Test
    void shouldReturnTrueWhenUserIsMember() {
        Long chatId = 1L;
        Long userId = 10L;

        when(personalChatService.isUserMember(chatId, userId))
                .thenReturn(true);

        var request =
                PersonalChat.ValidateUserIsMemberOfPersonalChatRequest
                        .newBuilder()
                        .setChatId(chatId)
                        .setUserId(userId)
                        .build();

        grpcService.validateUserIsMemberOfPersonalChat(
                request,
                validateObserver
        );

        ArgumentCaptor<PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> captor =
                ArgumentCaptor.forClass(
                        PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.class
                );

        verify(validateObserver).onNext(captor.capture());
        verify(validateObserver).onCompleted();

        var response = captor.getValue();

        assertEquals(chatId, response.getChatId());
        assertEquals(userId, response.getUserId());
        assertTrue(response.getIsMember());

        verifyNoMoreInteractions(validateObserver);
    }

    @Test
    void shouldReturnFalseWhenUserIsNotMember() {
        when(personalChatService.isUserMember(1L, 99L))
                .thenReturn(false);

        var request =
                PersonalChat.ValidateUserIsMemberOfPersonalChatRequest
                        .newBuilder()
                        .setChatId(1L)
                        .setUserId(99L)
                        .build();

        grpcService.validateUserIsMemberOfPersonalChat(
                request,
                validateObserver
        );

        ArgumentCaptor<
                PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> captor =
                ArgumentCaptor.forClass(
                        PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.class
                );

        verify(validateObserver).onNext(captor.capture());

        assertFalse(captor.getValue().getIsMember());
    }

    @Test
    void shouldReturnAllMembers() {
        List<Long> members = List.of(10L, 20L);

        when(personalChatService.getAllMembers(1L))
                .thenReturn(members);

        var request =
                PersonalChat.GetAllPersonalChatMembersRequest
                        .newBuilder()
                        .setChatId(1L)
                        .build();

        grpcService.getAllPersonalChatMembers(
                request,
                membersObserver
        );

        ArgumentCaptor<PersonalChat.GetAllPersonalChatMembersResponse> captor =
                ArgumentCaptor.forClass(
                        PersonalChat.GetAllPersonalChatMembersResponse.class
                );

        verify(membersObserver).onNext(captor.capture());
        verify(membersObserver).onCompleted();

        var response = captor.getValue();

        assertEquals(2, response.getUserIdCount());
        assertTrue(response.getUserIdList().containsAll(members));

        verifyNoMoreInteractions(membersObserver);
    }
}
