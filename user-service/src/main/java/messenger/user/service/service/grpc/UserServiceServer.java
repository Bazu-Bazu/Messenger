package messenger.user.service.service.grpc;

import com.messenger.grpc.User;
import com.messenger.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.user.service.repository.UserRepository;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class UserServiceServer extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void userExist(User.UserExistRequest request, StreamObserver<User.UserExistResponse> responseObserver) {
        Optional<messenger.user.service.entity.User> user = userRepository.findById(request.getUserId());
        if (user.isPresent()) {
            sendUserExistResponse(responseObserver, true, user.get().getUsername());
        } else {
            sendUserExistResponse(responseObserver, false, "");
        }
    }

    private void sendUserExistResponse(
            StreamObserver<User.UserExistResponse> responseObserver,
            boolean exist,
            String username
    ) {
        User.UserExistResponse response = User.UserExistResponse.newBuilder()
                .setUserExist(exist)
                .setUsername(username)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
