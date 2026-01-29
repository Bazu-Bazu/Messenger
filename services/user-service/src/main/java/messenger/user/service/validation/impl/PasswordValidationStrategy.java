package messenger.user.service.validation.impl;

import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import messenger.user.service.validation.ValidationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordValidationStrategy implements ValidationStrategy<String> {

    @Override
    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.trim().isEmpty()) {
            errors.add("Password cannot be empty");
            return new ValidationResult(false, errors);
        }

        if (password.length() < 8) {
            errors.add("Password must be at least characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one digit");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationType getType() {
        return ValidationType.PASSWORD;
    }

}
