package com.pixlabs.validation;

import com.pixlabs.web.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by pix-i on 16/01/2017.
 * ${Copyright}
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {


   public void initialize(PasswordMatches constraint) {

   }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      UserDto user = (UserDto) obj;
      //add matching pass?
      return user.getPassword().equals(user.getConfirmPassword());
   }
}
