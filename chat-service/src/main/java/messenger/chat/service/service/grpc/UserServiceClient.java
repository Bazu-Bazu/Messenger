package messenger.chat.service.service.grpc;

import com.messenger.grpc.User;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.messenger.grpc.UserServiceGrpc;

@Service
public class UserServiceClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public User.UserExistResponse userExist(Long userId) {
        User.UserExistRequest request =  User.UserExistRequest.newBuilder()
                .setUserId(userId)
                .build();

        return blockingStub.userExist(request);
    }

}
