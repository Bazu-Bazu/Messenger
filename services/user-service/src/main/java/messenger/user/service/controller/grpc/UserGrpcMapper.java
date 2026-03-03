package messenger.user.service.controller.grpc;

import messenger.user.service.dto.UserExistenceDto;
import messenger.user.service.dto.UserInfoDto;
import org.springframework.stereotype.Component;
import user.User;

@Component
public class UserGrpcMapper {

    public User.UsersExistResponse.UserExistence toGrpc(UserExistenceDto dto) {
        return User.UsersExistResponse.UserExistence.newBuilder()
                .setUserId(dto.userId())
                .setExists(dto.exists())
                .setIsActive(dto.isActive())
                .build();
    }

    public User.UsersInfoResponse.UserInfo toGrpc(UserInfoDto dto) {
        return User.UsersInfoResponse.UserInfo.newBuilder()
                .setUserId(dto.userId())
                .setUsername(dto.username())
                .setAvatarUrl(dto.avatarUrl())
                .build();
    }
}
