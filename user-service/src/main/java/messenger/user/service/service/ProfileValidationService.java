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
public class ProfileValidationService {

    private final List<ValidationStrategy<String>> stringStrategies;

    public void validateProfileUpdating(String firstName, String lastName, String bio) {
        List<String> errors = new ArrayList<>();

        ValidationResult fistNameResult = validateString(firstName, ValidationType.NAME);
        if (!fistNameResult.valid()) errors.addAll(fistNameResult.errors());

        ValidationResult lastNameResult = validateString(lastName, ValidationType.NAME);
        if (!lastNameResult.valid()) errors.addAll(lastNameResult.errors());

        ValidationResult bioResult = validateString(bio, ValidationType.BIO);
        if (!bioResult.valid()) errors.addAll(bioResult.errors());

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

}