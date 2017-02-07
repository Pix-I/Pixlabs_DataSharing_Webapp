package com.pixlabs.data.dao.user;

import com.pixlabs.data.entities.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 16/01/2017.
 * ${Copyright}
 */


public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
