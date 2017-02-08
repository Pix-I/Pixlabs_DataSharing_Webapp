package com.pixlabs.config;

import com.pixlabs.data.dao.user.PrivilegeRepository;
import com.pixlabs.data.dao.user.RoleRepository;
import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.entities.user.Privilege;
import com.pixlabs.data.entities.user.Role;
import com.pixlabs.data.entities.user.User;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by pix-i on 03/02/2017.
 * ${Copyright}
 */

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean setupDone = false;
    private PrivilegeRepository privilegeRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder encoder;


    /**
     * Creates a single admin user and the ADMIN and USER role.
     * @param contextRefreshedEvent Event raised when an ApplicationContext gets initialized or refreshed.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(setupDone)
            return;

        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege,writePrivilege,passwordPrivilege);
        createRoleIfNotFound("ROLE_ADMIN",adminPrivileges);
        createRoleIfNotFound("ROLE_USER",Arrays.asList(readPrivilege,passwordPrivilege));

        // Creating the admin
        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if(userRepository.findByUsername("admin")==null) {
            final User user = new User();
            user.setUsername("admin");
            user.setEmail("pix@pix-labs.com");
            user.setPassword(encoder.encode("testing"));
            user.setRoles(Collections.singletonList(adminRole));
            user.setEnabled(true);
            userRepository.save(user);
        }
        setupDone = true;

    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(final String name){
        Privilege privilege = privilegeRepository.findByName(name);
        if(privilege==null){
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges){
        Role role = roleRepository.findByName(name);
        if(role==null){
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Inject
    public void setPrivilegeRepository(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Inject
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Inject
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
