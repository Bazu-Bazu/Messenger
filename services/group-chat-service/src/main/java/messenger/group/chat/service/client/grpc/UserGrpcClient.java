package messenger.group.chat.service.client.grpc;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.UserExistenceDto;
import messenger.group.chat.service.dto.UserInfoDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    private final UserGrpcMapper userGrpcMapper;

    public List<UserExistenceDto> validateUsersExist(List<Long> userIds) {
        User.ValidateUsersExistRequest request = User.ValidateUsersExistRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        User.UsersExistResponse responses = blockingStub.validateUsersExist(request);

        return responses.getResultsList().stream()
                .map(userGrpcMapper::fromGrpc)
                .toList();
    }

    public List<UserInfoDto> getUsersInfo(List<Long> userIds) {
        User.UsersInfoRequest request = User.UsersInfoRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        User.UsersInfoResponse responses = blockingStub.usersInfo(request);

        return responses.getResultsList().stream()
                .map(userGrpcMapper::fromGrpc)
                .toList();
    }
}