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

    private final List<ValidationStrategy<String>> stringStrategies;

    public void validateUserRegistration(String username, String phone, String password) {
        List<String> errors = new ArrayList<>();

        ValidationResult usernameResult = validateString(username, ValidationType.USERNAME);
        if (!usernameResult.valid()) errors.addAll(usernameResult.errors());

        ValidationResult phoneResult = validateString(phone, ValidationType.PHONE);
        if (!phoneResult.valid()) errors.addAll(phoneResult.errors());

        ValidationResult passwordResult = validateString(password, ValidationType.PASSWORD);
        if (!passwordResult.valid()) errors.addAll(passwordResult.errors());

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed: ", errors);
        }
    }

    private ValidationResult validateString(String value, ValidationType type) {
        for (ValidationStrategy<String> strategy : stringStrategies) {
            if (strategy.getType().equals(type)) {
                return strategy.validate(value);
            }
        }

        throw new RuntimeException("Couldn't find the necessary strategy");
    }

    public void validatePhone(String phone) {
        ValidationResult phoneResult = validateString(phone, ValidationType.PHONE);

        if (!phoneResult.valid()) {
            throw new ValidationException("Validation failed: ", phoneResult.errors());
        }
    }

    public void validateUsername(String username) {
        ValidationResult usernameResult = validateString(username, ValidationType.USERNAME);

        if (!usernameResult.valid()) {
            throw new ValidationException("Validation failed: ", usernameResult.errors());
        }
    }

    public void validateEmail(String email) {
        ValidationResult emailResult = validateString(email, ValidationType.EMAIL);

        if (!emailResult.valid()) {
            throw new ValidationException("Validation failed: ", emailResult.errors());
        }
    }

    public void validatePassword(String password) {
        ValidationResult passwordResult = validateString(password, ValidationType.PASSWORD);

        if (!passwordResult.valid()) {
            throw new ValidationException("Validation failed: ", passwordResult.errors());
        }
    }

}