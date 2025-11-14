package messenger.user.service.dto.response;

import lombok.Builder;
import messenger.user.service.userEnum.UserStatus;

import java.time.Instant;

@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private UserStatus status;
    private ProfileResponse profile;

}
