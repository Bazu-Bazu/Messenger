package messenger.personal.chat.service.client.grpc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserGrpcClientTest {

    @Mock
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    @InjectMocks
    private UserGrpcClient userGrpcClient;

    @Test
    void shouldReturnUsersExistResponse() {
        List<Long> userIds = List.of(1L, 2L);

        User.UsersExistResponse mockResponse = User.UsersExistResponse.newBuilder()
                .addResults(
                        User.UsersExistResponse.UserExistence.newBuilder()
                                .setUserId(1L)
                                .setExists(true)
                                .setIsActive(true)
                                .build()
                )
                .addResults(
                        User.UsersExistResponse.UserExistence.newBuilder()
                                .setUserId(2L)
                                .setExists(true)
                                .setIsActive(true)
                                .build()
                )
                .build();

        when(blockingStub.validateUsersExist(any()))
                .thenReturn(mockResponse);

        User.UsersExistResponse response = userGrpcClient.validateUsersExist(userIds);

        assertNotNull(response);
        assertEquals(2, response.getResultsCount());
        assertTrue(response.getResultsList().stream().allMatch(User.UsersExistResponse.UserExistence::getExists));

        ArgumentCaptor<User.ValidateUsersExistRequest> captor = ArgumentCaptor.forClass(User.ValidateUsersExistRequest.class);
        verify(blockingStub).validateUsersExist(captor.capture());

        assertEquals(userIds, captor.getValue().getUserIdsList());
    }
}
