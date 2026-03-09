package messenger.group.chat.service.client.grpc;

import messenger.group.chat.service.dto.UserExistenceDto;
import messenger.group.chat.service.dto.UserInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGrpcClientTest {

    @Mock
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    @Mock
    private UserGrpcMapper userGrpcMapper;

    @InjectMocks
    private UserGrpcClient userGrpcClient;

    @BeforeEach
    void setup() {
        userGrpcClient = new UserGrpcClient(userGrpcMapper);
        ReflectionTestUtils.setField(userGrpcClient, "blockingStub", blockingStub);
    }

    @Test
    void validateUsersExist_success() {
        List<Long> userIds = List.of(1L, 2L);

        User.UsersExistResponse.UserExistence grpc1 = User.UsersExistResponse.UserExistence.newBuilder()
                .setUserId(1)
                .setExists(true)
                .setIsActive(true)
                .build();

        User.UsersExistResponse.UserExistence grpc2 = User.UsersExistResponse.UserExistence.newBuilder()
                .setUserId(2)
                .setExists(true)
                .setIsActive(false)
                .build();

        User.UsersExistResponse grpcResponse =
                User.UsersExistResponse.newBuilder()
                        .addResults(grpc1)
                        .addResults(grpc2)
                        .build();

        when(blockingStub.validateUsersExist(any())).thenReturn(grpcResponse);

        when(userGrpcMapper.fromGrpc(any(User.UsersExistResponse.UserExistence.class)))
                .thenAnswer(invocation -> {
                    User.UsersExistResponse.UserExistence arg = invocation.getArgument(0);
                    return new UserExistenceDto(arg.getUserId(), arg.getExists(), arg.getIsActive());
                });

        List<UserExistenceDto> result = userGrpcClient.validateUsersExist(userIds);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).userId());
        assertEquals(2L, result.get(1).userId());

        verify(blockingStub).validateUsersExist(any());
        verify(userGrpcMapper, times(2)).fromGrpc(any(User.UsersExistResponse.UserExistence.class));
    }

    @Test
    void getUsersInfo_success() {
        List<Long> userIds = List.of(1L);

        User.UsersInfoResponse.UserInfo grpcUser =
                User.UsersInfoResponse.UserInfo.newBuilder()
                        .setUserId(1)
                        .setUsername("john")
                        .build();

        User.UsersInfoResponse grpcResponse =
                User.UsersInfoResponse.newBuilder()
                        .addResults(grpcUser)
                        .build();

        when(blockingStub.usersInfo(any())).thenReturn(grpcResponse);

        when(userGrpcMapper.fromGrpc(any(User.UsersInfoResponse.UserInfo.class)))
                .thenAnswer(invocation -> {
                    User.UsersInfoResponse.UserInfo arg = invocation.getArgument(0);
                    return new UserInfoDto(arg.getUserId(), arg.getUsername(), "avatar");
                });

        List<UserInfoDto> result = userGrpcClient.getUsersInfo(userIds);

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).username());

        verify(blockingStub).usersInfo(any());
        verify(userGrpcMapper).fromGrpc(any(User.UsersInfoResponse.UserInfo.class));
    }
}