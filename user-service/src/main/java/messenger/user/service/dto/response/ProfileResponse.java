package messenger.user.service.dto.response;

import lombok.Builder;
import messenger.user.service.entity.Gender;

import java.time.LocalDate;

@Builder
public record ProfileResponse(
        Long userId,
        String firstName,
        String lastName,
        String bio,
        String avatarUrl,
        LocalDate birthDate,
        Integer age,
        Gender gender
) {}