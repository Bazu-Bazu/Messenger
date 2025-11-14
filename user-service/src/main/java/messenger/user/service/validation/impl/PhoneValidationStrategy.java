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
public class PhoneValidationStrategy implements ValidationStrategy<String> {

    private final UniquenessChecker uniquenessChecker;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    @Override
    public ValidationResult validate(String phone) {
        List<String> errors = new ArrayList<>();

        if (phone == null || phone.trim().isEmpty()) {
            errors.add("Phone cannot be empty");
            return new ValidationResult(false, errors);
        }

        if (!uniquenessChecker.isPhoneUnique(phone)) {
            errors.add("Phone already registered");
            return new ValidationResult(false, errors);
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            errors.add("Invalid phone number format");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

}
