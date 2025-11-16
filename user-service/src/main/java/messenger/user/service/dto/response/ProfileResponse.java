package messenger.user.service.dto.response;

import lombok.Builder;
import messenger.user.service.userEnum.Gender;

import java.time.LocalDate;

@Builder
public record ProfileResponse(
        String firstName,
        String lastName,
        String bio,
        String avatarUrl,
        LocalDate birthDate,
        Short age,
        Gender gender
) {}
