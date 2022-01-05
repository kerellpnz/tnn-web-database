package com.kerellpnz.tnnwebdatabase.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValveNumberValidator implements ConstraintValidator<ValveNumber, String> {

    private final String[] forbidden = {"О", "0", "O"};

    @Override
    public boolean isValid(String number, ConstraintValidatorContext arg1) {
        if (number != null) {
            for (String s : forbidden) {
                if (number.toUpperCase().startsWith(s))
                    return false;
            }
        }
        return true;
    }
}
