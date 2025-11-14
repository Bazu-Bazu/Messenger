package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.exception.ValidationException;
import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import messenger.user.service.validation.ValidationType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final List<ValidationStrategy<Object>> strategies;

    public void validationRegisterUser(String username, String phone, String password) {
        List<String> errors = new ArrayList<>();

        ValidationResult usernameResult = validateField(username, ValidationType.USERNAME);
        if (!usernameResult.valid()) errors.addAll(usernameResult.errors());

        ValidationResult phoneResult = validateField(phone, ValidationType.PHONE);
        if (!phoneResult.valid()) errors.addAll(usernameResult.errors());

        ValidationResult passwordResult = validateField(password, ValidationType.PHONE);
        if (!passwordResult.valid()) errors.addAll(usernameResult.errors());

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: ", errors);
        }
    }

    private ValidationResult validateField(String value, ValidationType type) {
        for (ValidationStrategy<Object> strategy : strategies) {
            if (strategy.getClass().getSimpleName().toUpperCase().contains(type.toString())) {
                return strategy.validate(value);
            }
        }

        throw new ValidationException("Couldn't find the necessary strategy");
    }

}
