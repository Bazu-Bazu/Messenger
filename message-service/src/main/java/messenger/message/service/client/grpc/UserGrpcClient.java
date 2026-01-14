package messenger.message.service.client.grpc;

import exception.UserIsNotActive;
import exception.UserNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

@Component
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public void validateUsersExist(List<Long> userIds) {
        var request = User.ValidateUsersExistRequest.newBuilder()
                .addAllUserIds(userIds)
                .build();

        var response = blockingStub.validateUsersExist(request);

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

}
