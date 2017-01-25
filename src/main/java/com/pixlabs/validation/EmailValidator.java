package com.pixlabs.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pix-i on 16/01/2017.
 * ${Copyright}
 */
public class EmailValidator implements ConstraintValidator<ValidEmail,String>{

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]"+
            "(.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*" +
            "(.[A-Za-z]{2,})$";


    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    private boolean validateEmail(String value) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
