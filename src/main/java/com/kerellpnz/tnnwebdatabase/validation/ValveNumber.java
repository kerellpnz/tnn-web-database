package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValveNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValveNumber {

    String message() default "Начало с \"О\" или \"0\" запрещено!!!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
