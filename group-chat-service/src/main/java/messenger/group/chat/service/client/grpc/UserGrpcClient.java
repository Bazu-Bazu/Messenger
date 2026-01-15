package messenger.group.chat.service.client.grpc;

import dto.response.UserInfo;
import exception.UserIsNotActive;
import exception.UserNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import user.User;
import user.UserServiceGrpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public void validateUsersExist(List<Long> userIds) {
        User.ValidateUsersExistRequest request =  User.ValidateUsersExistRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        User.UsersExistResponse response = blockingStub.validateUsersExist(request);

        response.getResultsList()
                .forEach(result -> {
                    if (!result.getExists()) {
                        throw new UserNotFoundException(
                                String.format("User with id %d not found", result.getUserId())
                        );
                    }

                    if (!result.getIsActive()) {
                        throw new UserIsNotActive(
                                String.format("User with id %d not active", result.getUserId())
                        );
                    }
                });
    }

    public Map<Long, UserInfo> getUsersInfo(List<Long> userIds) {
        User.UsersInfoRequest request = User.UsersInfoRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        User.UsersInfoResponse response = blockingStub.usersInfo(request);

        Map<Long, UserInfo> usersInfoMap = new HashMap<>();

        response.getResultsList()
                .forEach(result -> {
                    UserInfo info = UserInfo.builder()
                            .id(result.getUserId())
                            .username(result.getUsername())
                            .avatarUrl(result.getAvatarUrl())
                            .bio(result.getBio())
                            .status(result.getStatus())
                            .build();

                    usersInfoMap.put(result.getUserId(), info);
                });

        return usersInfoMap;
    }

}