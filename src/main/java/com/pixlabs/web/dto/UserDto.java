package com.pixlabs.web.dto;

import com.pixlabs.validation.PasswordMatches;
import com.pixlabs.validation.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */



@PasswordMatches
public class UserDto {

    @NotNull
    @Size(min = 3)
    private String regUsername = "User";

    @NotNull
    @Size(min = 2)
    @ValidEmail
    private String email = "a@a.a";

    @NotNull
    @Size(min = 1)
    private String password = "password123";

    @NotNull
    @Size(min = 1)
    private String confirmPassword = "password123";

    public String getRegUsername() {
        return regUsername;
    }

    public void setRegUsername(String regUsername) {
        this.regUsername = regUsername;
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
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "regUsername='" + regUsername + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
