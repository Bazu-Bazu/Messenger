package messenger.user.service.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import messenger.user.service.validation.validator.UniqueEmailValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "Email already taken";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
