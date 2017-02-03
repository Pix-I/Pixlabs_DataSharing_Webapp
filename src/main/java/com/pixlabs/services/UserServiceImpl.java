package com.pixlabs.services;

import com.pixlabs.data.dao.PasswordResetTokenRepository;
import com.pixlabs.data.dao.RoleRepository;
import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.dao.VerificationTokenRepository;
import com.pixlabs.data.entities.PasswordResetToken;
import com.pixlabs.data.entities.User;
import com.pixlabs.data.entities.VerificationToken;
import com.pixlabs.exceptions.UserAlreadyExistException;
import com.pixlabs.util.GravatarUtil;
import com.pixlabs.web.dto.UserDto;
import com.pixlabs.web.dto.UserPrefDto;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     * A default profile picture is also generated based on the email address using the jgravatar library to create
     * an identicon.
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

        //Add profile picture
        user.setProfilePicture(GravatarUtil.getGravatar(userDto.getEmail()));
        //Add roles
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));


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

    /**
     * Updates the password of a given user
     * @param user User who needs to update its password
     * @param password A password
     */

    @Override
    public void updateUserPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    /**
     * Updates the prefs of an user using a userPrefDto object. It checks if a field is null and update it on
     * the user object if it's not.
     * @param userPrefDto Object containing the new preferences.
     * @param user User object to be updated.
     */
    @Override
    public void updatePrefs(UserPrefDto userPrefDto, User user) throws ParseException {
        if(!userPrefDto.getBirthDate().equals("")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            user.setBirthDate(dateFormat.parse(userPrefDto.getBirthDate()));
        }
        if(!userPrefDto.getEmail().equals("")){
            user.setEmail(userPrefDto.getEmail());
        }
        if(userPrefDto.getPassword().length()>1){
            user.setPassword(encoder.encode(userPrefDto.getPassword()));
        }
        userRepository.save(user);
    }

    private SessionRegistry sessionRegistry;

    @Override
    public List<String> getUsersFromRegistry(){
        return sessionRegistry.getAllPrincipals().stream()
                .filter(u-> !sessionRegistry.getAllSessions(u,false).isEmpty())
                .map(Object::toString)
                .collect(Collectors.toList());
    }


    @Inject
    public void setResetTokenRepository(PasswordResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Inject
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Inject
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
