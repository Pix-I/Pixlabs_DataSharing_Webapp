package com.pixlabs.validation;

import com.pixlabs.web.dto.IPassword;

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
      if(obj instanceof IPassword){
         IPassword dto = (IPassword) obj;
         return dto.getPassword().equals(dto.getConfirmPassword());
      }

      //add matching pass?
      return false;
   }
}
