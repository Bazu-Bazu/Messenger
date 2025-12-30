package messenger.user.service.service.grpc;

import com.messenger.grpc.User;
import com.messenger.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.user.service.entity.UserStatus;
import messenger.user.service.repository.UserRepository;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServer extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void validateUsersExist(
            User.ValidateUsersExistRequest request,
            StreamObserver<User.UsersExistResponse> responseObserver
    ) {
        List<Long> userIds = request.getUserIdsList();

        List<User.UsersExistResponse.UserExistence> results = checkUsersExistence(userIds);

        User.UsersExistResponse response = User.UsersExistResponse.newBuilder()
                .addAllResults(results)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private List<User.UsersExistResponse.UserExistence> checkUsersExistence(List<Long> userIds) {
        List<messenger.user.service.entity.User> users = userRepository.findAllById(userIds);

        Map<Long, messenger.user.service.entity.User> userMap = users.stream()
                .collect(Collectors.toMap(
                        messenger.user.service.entity.User::getId,
                        Function.identity()
                ));

        return userIds.stream()
                .map(userId -> {
                    messenger.user.service.entity.User user = userMap.get(userId);

                    if (user == null) {
                        return User.UsersExistResponse.UserExistence.newBuilder()
                                .setUserId(userId)
                                .setExists(false)
                                .setIsActive(false)
                                .build();
                    }

                    return User.UsersExistResponse.UserExistence.newBuilder()
                            .setUserId(userId)
                            .setExists(true)
                            .setIsActive(user.getStatus().equals(UserStatus.ACTIVE))
                            .build();
                })
                .toList();
    }

}
