package com.pixlabs.data.dao.user;

import com.pixlabs.data.entities.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
