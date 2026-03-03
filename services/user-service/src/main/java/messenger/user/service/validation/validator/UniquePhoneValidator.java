package messenger.user.service.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.repository.UserRepository;
import messenger.user.service.validation.annotation.UniquePhone;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        return !userRepository.existsByPhone(phone);
    }
}
