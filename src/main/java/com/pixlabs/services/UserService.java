package com.pixlabs.services;

import com.pixlabs.data.dao.RoleRepository;
import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.dao.VerificationTokenRepository;
import com.pixlabs.data.entities.PasswordResetToken;
import com.pixlabs.data.entities.User;
import com.pixlabs.exceptions.UserAlreadyExistException;
import com.pixlabs.web.dto.UserDto;
import com.pixlabs.web.dto.UserPrefDto;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */
public interface UserService {

    @Inject
    void setUserRepository(UserRepository userRepository);

    void createVerificationToken(User user, String token);

    User registerAccount(UserDto userDto) throws UserAlreadyExistException;

    @Inject
    void setRoleRepository(RoleRepository roleRepository);

    void saveUser(User user);

    @Inject
    void setTokenRepository(VerificationTokenRepository tokenRepository);

    boolean validateConfirmationToken(String token);

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    void createResetToken(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    void updateUserPassword(User user, String password);

    void updatePrefs(UserPrefDto userPrefDto, User user) throws ParseException;

    List<String> getUsersFromRegistry();


    // Add stuff to find users

}
