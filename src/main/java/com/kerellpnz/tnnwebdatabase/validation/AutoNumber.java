package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AutoNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoNumber {

    String message() default "Номер не соответствует указанной форме!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
