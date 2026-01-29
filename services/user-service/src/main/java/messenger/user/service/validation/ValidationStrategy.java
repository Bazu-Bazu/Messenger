package messenger.user.service.validation;

import org.springframework.stereotype.Component;

@Component
public interface ValidationStrategy<String> {

    ValidationResult validate(String value);
    ValidationType getType();

}