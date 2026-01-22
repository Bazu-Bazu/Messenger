package messenger.user.service.controller.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.enums.UserStatus;
import messenger.user.service.domain.repository.UserRepository;
import net.devh.boot.grpc.server.service.GrpcService;
import user.User;
import user.UserServiceGrpc;

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
        List<messenger.user.service.domain.entity.User> users = userRepository.findAllById(userIds);

        Map<Long, messenger.user.service.domain.entity.User> userMap = users.stream()
                .collect(Collectors.toMap(
                        messenger.user.service.domain.entity.User::getId,
                        Function.identity()
                ));

        return userIds.stream()
                .map(userId -> {
                    messenger.user.service.domain.entity.User user = userMap.get(userId);

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

    @Override
    public void usersInfo(User.UsersInfoRequest request, StreamObserver<User.UsersInfoResponse> responseObserver) {
        List<Long> userIds = request.getUserIdsList();

        List<User.UsersInfoResponse.UserInfo> results = getUsersInfo(userIds);

        User.UsersInfoResponse response = User.UsersInfoResponse.newBuilder()
                .addAllResults(results)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private List<User.UsersInfoResponse.UserInfo> getUsersInfo(List<Long> userIds) {
        List<messenger.user.service.domain.entity.User> users = userRepository.findAllById(userIds);

        return users.stream()
                .map(user -> User.UsersInfoResponse.UserInfo.newBuilder()
                        .setUserId(user.getId())
                        .setUsername(user.getUsername())
                        .setAvatarUrl(user.getProfile().getAvatarUrl() != null ? user.getProfile().getAvatarUrl() : "")
                        .setBio(user.getProfile().getBio() != null ? user.getProfile().getBio() : "")
                        .setStatus(user.getStatus().toString())
                        .build())
                .toList();
    }

}
