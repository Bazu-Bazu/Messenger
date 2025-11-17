package messenger.user.service.dto.request;

import lombok.Builder;
import messenger.user.service.userEnum.Gender;

import java.time.LocalDate;

@Builder
public record UpdateProfileRequest (
         String firstName,
         String lastName,
         String bio,
         LocalDate birthDate,
         Gender gender
) {}
