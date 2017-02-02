package com.pixlabs.services;

/**
 * Created by pix-i on 29/01/2017.
 * ${Copyright}
 */
public interface SecurityService {
    String validatePasswordResetToken(long id, String token);
    String findLoggedInUsername();
}
