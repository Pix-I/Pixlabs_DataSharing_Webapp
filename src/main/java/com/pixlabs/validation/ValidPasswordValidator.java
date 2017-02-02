package com.pixlabs.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by pix-i on 02/02/2017.
 * ${Copyright}
 */
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {


    public void initialize(ValidPassword constraint) {

    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    private boolean validateEmail(String value) {
        return value.length()>1;
    }
}


