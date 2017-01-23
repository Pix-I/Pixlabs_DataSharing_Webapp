package com.pixlabs.data.dao;

import com.pixlabs.data.entities.Role;
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
