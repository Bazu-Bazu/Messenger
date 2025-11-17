package messenger.user.service.validation.impl;

import messenger.user.service.validation.ValidationResult;
import messenger.user.service.validation.ValidationStrategy;
import messenger.user.service.validation.ValidationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class NameValidationStrategy implements ValidationStrategy<String> {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Zа-яА-Я]{2,25}$");

    @Override
    public ValidationResult validate(String name) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(true, errors);
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            errors.add("Name must be 2-25 characters and contain only letters");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationType getType() {
        return ValidationType.NAME;
    }

}