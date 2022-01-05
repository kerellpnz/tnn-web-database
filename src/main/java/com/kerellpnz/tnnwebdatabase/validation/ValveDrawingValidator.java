package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValveDrawingValidator implements ConstraintValidator<ValveDrawing, String> {

    @Override
    public boolean isValid(String drawing, ConstraintValidatorContext arg1) {
        if (drawing != null) {
            return drawing.matches("^(ПТ \\d{5}-\\d{3,4}(|[А-Я]\\d?))$");
        }
        return true;
    }
}
