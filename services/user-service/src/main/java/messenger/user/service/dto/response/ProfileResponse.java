package messenger.user.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.user.service.domain.enums.Gender;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private LocalDate birthDate;
    private Integer age;
    private Gender gender;
}