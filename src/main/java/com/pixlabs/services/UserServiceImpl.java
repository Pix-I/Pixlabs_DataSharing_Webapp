package com.pixlabs.services;

import com.pixlabs.data.dao.PasswordResetTokenRepository;
import com.pixlabs.data.dao.RoleRepository;
import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.dao.VerificationTokenRepository;
import com.pixlabs.data.entities.PasswordResetToken;
import com.pixlabs.data.entities.User;
import com.pixlabs.data.entities.VerificationToken;
import com.pixlabs.exceptions.UserAlreadyExistException;
import com.pixlabs.web.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    @Inject
    @Override
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private VerificationTokenRepository tokenRepository;
    private PasswordEncoder encoder;


    /**
     * Creates and saves a new token to the verificationTokenRepository
     * @param user The user object to be associated with the token
     * @param token A random UUID to string.
     */
    @Override
    public void createVerificationToken(final User user, final String token){
        final VerificationToken verificationToken = new VerificationToken(user,token);
        tokenRepository.save(verificationToken);
    }


    /**
     * Registers a new account into the database based on a userDto object containing the most important details.
     * @param userDto contains an username, email address and password.
     * @return A newly created user object.
     * @throws UserAlreadyExistException If there's already an user with the email/username in the DB.
     */
    @Override
    public User registerAccount(UserDto userDto) throws UserAlreadyExistException {
        if(emailAlreadyExist(userDto.getEmail())){
            throw new UserAlreadyExistException(
                    "EmailTaken");
        }
        if(usernameAlreadyExist(userDto.getRegUsername())){
            throw new UserAlreadyExistException(
                    "UsernameTaken");
        }
        final User user = new User();
        user.setUsername(userDto.getRegUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        //Add roles


        //Add email verification
//        user.setEnabled(true);

        return userRepository.save(user);
    }

    /**
     * Checks if a given email already exists in the userRepository
     * @param email A string representing an email
     * @return True only if an email corresponding the given string is found in the DB.
     */
    private boolean emailAlreadyExist(String email) {
        return (userRepository.findByEmail(email) !=null );

    }

    private boolean usernameAlreadyExist(String username) {
        return (userRepository.findByUsername(username)!=null);

    }

    @Override
    @Inject
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveUser(final User user) {
        userRepository.save(user);
    }

    @Inject
    @Override
    public void setTokenRepository(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Validates an email confirmation token
     * @param token A random UUID that should be associated with an user
     * @return returns false if the token isn't associated with an user
     */

    @Override
    public boolean validateConfirmationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken==null){
            System.out.println("Invalid token.");
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return true;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private PasswordResetTokenRepository resetTokenRepository;


    /**
     * Creates a password reset token
      * @param user The user object to be associated with the reset token
     * @param token A random string
     */
    @Override
    public void createResetToken(User user, String token) {
        final PasswordResetToken resetToken = new PasswordResetToken(token,user);
        resetTokenRepository.save(resetToken);
    }
    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return resetTokenRepository.findByToken(token);
    }

    @Inject
    public void setResetTokenRepository(PasswordResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Inject
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }
}
