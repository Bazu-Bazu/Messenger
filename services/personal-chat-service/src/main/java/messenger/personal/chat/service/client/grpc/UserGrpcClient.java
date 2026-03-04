package messenger.personal.chat.service.client.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

@Component
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public User.UsersExistResponse validateUsersExist(List<Long> userIds) {
        User.ValidateUsersExistRequest request = User.ValidateUsersExistRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        return blockingStub.validateUsersExist(request);
    }
}
