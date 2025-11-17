package messenger.user.service.validation.impl;

import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import messenger.user.service.validation.ValidationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class BioValidationStrategy implements ValidationStrategy<String> {

    private static final Pattern BIO_PATTERN = Pattern.compile("^{1,500}$");

    @Override
    public ValidationResult validate(String bio) {
        List<String> errors = new ArrayList<>();

        if (bio == null || bio.trim().isEmpty()) {
            return new ValidationResult(true, errors);
        }

        if (!BIO_PATTERN.matcher(bio).matches()) {
            errors.add("Bio must not exceed 500 characters");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationType getType() {
        return ValidationType.BIO;
    }

}