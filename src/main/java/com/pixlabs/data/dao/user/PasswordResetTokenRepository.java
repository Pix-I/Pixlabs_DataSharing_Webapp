package com.pixlabs.data.dao.user;

import com.pixlabs.data.entities.user.PasswordResetToken;
import com.pixlabs.data.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by pix-i on 22/01/2017.
 * ${Copyright}
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

}
