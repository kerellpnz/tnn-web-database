package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValveDrawingValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValveDrawing {
    String message() default "Только номер чертежа! С \"М\" или без!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
