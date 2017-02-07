package com.pixlabs.services;

import com.pixlabs.data.dao.user.PasswordResetTokenRepository;
import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.entities.user.PasswordResetToken;
import com.pixlabs.data.entities.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Created by pix-i on 29/01/2017.
 * ${Copyright}
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    private PasswordResetTokenRepository resetTokenRepository;
    private DaoAuthenticationProvider authProvider;
    private UserRepository userRepository;
    private PasswordEncoder encoder;


    @Inject
    public void setResetTokenRepository(PasswordResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Override
    public String validatePasswordResetToken(long id, String token){
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token);
        if((resetToken==null)||(resetToken.getUser().getId()!=id))
            return null;
        //Add expiration


        //
        User user = resetToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Arrays.asList(
                        new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return user.getUsername();
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if(userDetails instanceof UserDetails){
            return ((UserDetails) userDetails).getUsername();
        }
        return null;
    }

    @Inject
    public void setAuthProvider(DaoAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Inject
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }
}
