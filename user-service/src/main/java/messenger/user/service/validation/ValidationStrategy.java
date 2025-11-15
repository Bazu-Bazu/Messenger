package messenger.user.service.validation;

import org.springframework.stereotype.Component;

@Component
public interface ValidationStrategy<T> {

    ValidationResult validate(T value);
    ValidationType getType();

}
