package messenger.user.service.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import messenger.user.service.domain.enums.Gender;

import java.time.LocalDate;

@Builder
public record UpdateProfileRequest (

        @Size(min = 2, max = 25, message = "First name must be 2-25 characters")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Username must contain only letters")
        String firstName,

        @Size(min = 2, max = 25, message = "Last name must be 2-25 characters")
        @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Username must contain only letters")
        String lastName,

        @Size(min = 1, max = 500, message = "Bio should not exceed 500 characters")
        String bio,

        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        Gender gender
) {}
