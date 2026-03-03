package messenger.user.service.controller.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.UserExistenceDto;
import messenger.user.service.dto.UserInfoDto;
import messenger.user.service.service.UserService;
import net.devh.boot.grpc.server.service.GrpcService;
import user.User;
import user.UserServiceGrpc;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServer extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UserGrpcMapper userGrpcMapper;

    @Override
    public void validateUsersExist(
            User.ValidateUsersExistRequest request,
            StreamObserver<User.UsersExistResponse> responseObserver
    ) {
        List<UserExistenceDto> dtoList = userService.validateUserExist(request.getUserIdsList());

        List<User.UsersExistResponse.UserExistence> grpcList = dtoList.stream()
                .map(userGrpcMapper::toGrpc)
                .toList();

        User.UsersExistResponse response = User.UsersExistResponse.newBuilder()
                .addAllResults(grpcList)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void usersInfo(
            User.UsersInfoRequest request,
            StreamObserver<User.UsersInfoResponse> responseObserver
    ) {
        List<UserInfoDto> dtoList = userService.getUsersInfo(request.getUserIdsList());

        List<User.UsersInfoResponse.UserInfo> grpcList = dtoList.stream()
                .map(userGrpcMapper::toGrpc)
                .toList();

        User.UsersInfoResponse response = User.UsersInfoResponse.newBuilder()
                .addAllResults(grpcList)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
