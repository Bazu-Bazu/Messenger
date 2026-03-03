package messenger.user.service.controller.grpc;

import io.grpc.stub.StreamObserver;
import messenger.user.service.dto.UserExistenceDto;
import messenger.user.service.dto.UserInfoDto;
import messenger.user.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserGrpcServerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserGrpcMapper userGrpcMapper;

    @InjectMocks
    private UserGrpcServer grpcServer;

    @Test
    void validateUsersExist_shouldReturnGrpcResponse() {
        List<Long> userIds = List.of(1L, 2L);
        User.ValidateUsersExistRequest request = User.ValidateUsersExistRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        UserExistenceDto dto1 = UserExistenceDto.builder().userId(1L).exists(true).isActive(true).build();
        UserExistenceDto dto2 = UserExistenceDto.builder().userId(2L).exists(false).isActive(false).build();
        List<UserExistenceDto> dtoList = List.of(dto1, dto2);

        when(userService.validateUserExist(userIds)).thenReturn(dtoList);

        User.UsersExistResponse.UserExistence grpc1 = User.UsersExistResponse.UserExistence.newBuilder()
                .setUserId(1L).setExists(true).setIsActive(true).build();
        User.UsersExistResponse.UserExistence grpc2 = User.UsersExistResponse.UserExistence.newBuilder()
                .setUserId(2L).setExists(false).setIsActive(false).build();

        when(userGrpcMapper.toGrpc(dto1)).thenReturn(grpc1);
        when(userGrpcMapper.toGrpc(dto2)).thenReturn(grpc2);

        StreamObserver<User.UsersExistResponse> observer = mock(StreamObserver.class);

        grpcServer.validateUsersExist(request, observer);

        verify(observer).onNext(any(User.UsersExistResponse.class));
        verify(observer).onCompleted();
    }

    @Test
    void usersInfo_shouldReturnGrpcResponse() {
        List<Long> userIds = List.of(1L, 2L);
        User.UsersInfoRequest request = User.UsersInfoRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        UserInfoDto dto1 = UserInfoDto.builder().userId(1L).username("john").avatarUrl("url1").build();
        UserInfoDto dto2 = UserInfoDto.builder().userId(2L).username("jane").avatarUrl("url2").build();
        List<UserInfoDto> dtoList = List.of(dto1, dto2);

        when(userService.getUsersInfo(userIds)).thenReturn(dtoList);

        User.UsersInfoResponse.UserInfo grpc1 = User.UsersInfoResponse.UserInfo.newBuilder()
                .setUserId(1L).setUsername("john").setAvatarUrl("url1").build();
        User.UsersInfoResponse.UserInfo grpc2 = User.UsersInfoResponse.UserInfo.newBuilder()
                .setUserId(2L).setUsername("jane").setAvatarUrl("url2").build();

        when(userGrpcMapper.toGrpc(dto1)).thenReturn(grpc1);
        when(userGrpcMapper.toGrpc(dto2)).thenReturn(grpc2);

        StreamObserver<User.UsersInfoResponse> observer = mock(StreamObserver.class);

        grpcServer.usersInfo(request, observer);

        verify(observer).onNext(any(User.UsersInfoResponse.class));
        verify(observer).onCompleted();
    }
}
