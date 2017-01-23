package com.pixlabs.data.dao;

import com.pixlabs.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 16/01/2017.
 * ${Copyright}
 */


public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
    User findByUsername(String username);


    @Override
    void delete(User user);


}
