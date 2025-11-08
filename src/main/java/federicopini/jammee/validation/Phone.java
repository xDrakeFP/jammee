package federicopini.jammee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface Phone {
    String message() default "Numero di telefono non valido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String region()default "IT";
}
