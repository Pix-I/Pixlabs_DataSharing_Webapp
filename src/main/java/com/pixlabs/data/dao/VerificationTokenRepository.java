package com.pixlabs.data.dao;

import com.pixlabs.data.entities.User;
import com.pixlabs.data.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 20/01/2017.
 * ${Copyright}
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {


    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);



}
