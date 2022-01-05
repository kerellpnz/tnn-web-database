package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AutoNumberValidator implements ConstraintValidator<AutoNumber, String> {

    @Override
    public boolean isValid(String number, ConstraintValidatorContext arg1) {
        if (number != null) {
            if (number.matches("^([А-Я] \\d{3} [А-Я]{2} \\d{2,3})$"))
                return true;
            else return number.matches("^(Ж/д вагон №\\d{7,8})$");
        }
        return true;
    }
}
