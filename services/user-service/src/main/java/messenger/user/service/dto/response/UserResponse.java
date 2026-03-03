package messenger.user.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.user.service.domain.enums.UserStatus;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private UserStatus status;
}
