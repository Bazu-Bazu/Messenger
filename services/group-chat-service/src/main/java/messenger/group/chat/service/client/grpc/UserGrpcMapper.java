package messenger.group.chat.service.client.grpc;

import messenger.group.chat.service.dto.UserExistenceDto;
import messenger.group.chat.service.dto.UserInfoDto;
import org.springframework.stereotype.Component;
import user.User;

@Component
public class UserGrpcMapper {

    public UserExistenceDto fromGrpc(User.UsersExistResponse.UserExistence existence) {
        return UserExistenceDto.builder()
                .userId(existence.getUserId())
                .exists(existence.getExists())
                .isActive(existence.getIsActive())
                .build();
    }

    public UserInfoDto fromGrpc(User.UsersInfoResponse.UserInfo dto) {
        return UserInfoDto.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .avatarId(dto.getAvatarId())
                .build();
    }
}