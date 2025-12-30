package messenger.personal.chat.service.client.grpc;

import com.messenger.grpc.User;
import com.messenger.grpc.UserServiceGrpc;
import exception.UserIsNotActive;
import exception.UserNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
