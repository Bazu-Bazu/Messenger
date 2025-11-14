package messenger.user.service.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import messenger.user.service.userEnum.Gender;

import java.time.LocalDate;

@Embeddable
public class Profile {

    private String firstName;
    private String lastName;

    @Column(length = 500)
    private String bio;

    private String avatarUrl;
    private LocalDate birthDate;
    private Short age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
