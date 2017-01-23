package com.pixlabs.security;

import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.entities.Privilege;
import com.pixlabs.data.entities.Role;
import com.pixlabs.data.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {


    @Inject
    public void setUserRepository(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    public UserDetailsServiceImpl(){
        super();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            final User user = userRepository.findByUsername(username);
            if(user == null){
                throw new UsernameNotFoundException("Username " + username + " not found.");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    true,
                    getAuthorities(user.getRoles())
                    );
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getUsername());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
