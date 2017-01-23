package com.pixlabs.security;

import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.entities.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.inject.Inject;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

public class AuthenticationProviderImpl extends DaoAuthenticationProvider {

    private UserRepository userRepository;



    //Adding email login is kinda annoying because that would mean rewriting the super authenticate method afaik.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        System.out.println("User:" + authentication.getName());
        final User
            user = userRepository.findByEmail(authentication.getName());
        if(user == null){
            System.out.println("Bad username and or password.");
            throw new BadCredentialsException("Invalid username or password");
        }
        System.out.println("Log:" + authentication.getName());
        final Authentication result = super.authenticate(authentication);
        return new UsernamePasswordAuthenticationToken(user,result.getCredentials(),result.getAuthorities());

    }




    @Inject
    public AuthenticationProviderImpl setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }
}
