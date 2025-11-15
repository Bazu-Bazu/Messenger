package messenger.user.service.validation.impl;

import lombok.RequiredArgsConstructor;
import messenger.user.service.validation.UniquenessChecker;
import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import messenger.user.service.validation.ValidationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EmailValidationStrategy implements ValidationStrategy<String> {

    private final UniquenessChecker uniquenessChecker;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public ValidationResult validate(String email) {
        List<String> errors = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email cannot be empty");
            return new ValidationResult(false, errors);
        }

        if (!uniquenessChecker.isEmailUnique(email)) {
            errors.add("Email already registered");
            return new ValidationResult(false, errors);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format");
        }

        if (email.length() > 255) {
            errors.add("Email is too long");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationType getType() {
        return ValidationType.EMAIL;
    }

}
