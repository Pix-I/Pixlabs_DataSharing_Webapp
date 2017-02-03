package com.pixlabs.web.dto;

import com.pixlabs.validation.PasswordMatches;
import com.pixlabs.validation.ValidEmail;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;

/**
 * Created by pix-i on 02/02/2017.
 * ${Copyright}
 */
@PasswordMatches
public class UserPrefDto implements IPassword {


    @Past
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String birthDate;
    @ValidEmail
    private String email;
    private String password;
    private String passwordConfirm;

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return passwordConfirm;
    }

    public void setConfirmPassword(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
