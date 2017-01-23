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



    @Override
    public void createVerificationToken(final User user, final String token){
        final VerificationToken verificationToken = new VerificationToken(user,token);
        System.out.println("Saving token!");
        tokenRepository.save(verificationToken);
    }




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
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        //Add email verification
        user.setEnabled(true);

        return userRepository.save(user);
    }

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

    @Override
    public boolean validateToken(String token) {
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
}
