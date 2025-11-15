package messenger.user.service.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.user.service.userEnum.Gender;

import java.time.LocalDate;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Column(length = 25)
    private String firstName;

    @Column(length = 25)
    private String lastName;

    @Column(length = 500)
    private String bio;

    private String avatarUrl;
    private LocalDate birthDate;
    private Short age;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Gender gender;

}
