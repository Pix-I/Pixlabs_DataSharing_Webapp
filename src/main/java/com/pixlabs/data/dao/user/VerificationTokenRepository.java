package com.pixlabs.data.dao.user;

import com.pixlabs.data.entities.user.User;
import com.pixlabs.data.entities.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 20/01/2017.
 * ${Copyright}
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {


    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);



}
