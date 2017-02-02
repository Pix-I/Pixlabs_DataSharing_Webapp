package com.pixlabs.web.dto;

import com.pixlabs.validation.PasswordMatches;
import com.pixlabs.validation.ValidPassword;

import javax.validation.constraints.NotNull;

/**
 * Created by pix-i on 27/01/2017.
 * ${Copyright}
 */

@PasswordMatches
public class PasswordResetDto implements IPassword{

    @NotNull
    @ValidPassword
    private String newPassword;
    @NotNull
    private String newPasswordConfirmation;


    public String getPassword() {
        return newPassword;
    }

    public void setPassword(String password) {
        this.newPassword = password;
    }

    public String getConfirmPassword() {
        return newPasswordConfirmation;
    }

    public void setConfirmPassword(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
