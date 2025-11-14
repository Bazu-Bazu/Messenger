package messenger.user.service.validation.impl;

import lombok.RequiredArgsConstructor;
import messenger.user.service.validation.UniquenessChecker;
import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UsernameValidationStrategy implements ValidationStrategy<String> {

    private final UniquenessChecker uniquenessChecker;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,25}$");

    @Override
    public ValidationResult validate(String username) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add("Username cannot be empty");
            return new ValidationResult(false, errors);
        }

        if (!uniquenessChecker.isUsernameUnique(username)) {
            errors.add("Username already taken");
            return new ValidationResult(false, errors);
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.add("Username must be 5-25 characters and contain only letters, numbers and underscores");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

}
