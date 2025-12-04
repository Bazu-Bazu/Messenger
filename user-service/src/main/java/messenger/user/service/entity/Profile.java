package messenger.user.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import messenger.user.service.dto.request.UpdateProfileRequest;

import java.time.LocalDate;
import java.time.Period;

@Embeddable
@Data
public class Profile {

    @Column(length = 25)
    private String firstName;

    @Column(length = 25)
    private String lastName;

    @Column(length = 500)
    private String bio;

    private String avatarUrl;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Gender gender;

    public void updateFrom(UpdateProfileRequest request) {
        if (request.firstName() != null) {
            this.firstName = request.firstName();
        }
        if (request.lastName() != null) {
            this.lastName = request.lastName();
        }
        if (request.bio() != null) {
            this.bio = request.bio();
        }
        if (request.birthDate() != null) {
            this.birthDate = request.birthDate();
        }
        if (request.gender() != null) {
            this.gender = request.gender();
        }
    }

    public Integer getAge() {
        if (this.birthDate == null) return null;

        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

}