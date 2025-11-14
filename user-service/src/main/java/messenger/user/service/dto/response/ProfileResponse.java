package messenger.user.service.dto.response;

import lombok.Builder;
import messenger.user.service.userEnum.Gender;

import java.time.LocalDate;

@Builder
public class ProfileResponse {

    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private LocalDate birthDate;
    private Short age;
    private Gender gender;

}
