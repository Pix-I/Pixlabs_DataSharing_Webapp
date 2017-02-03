package com.pixlabs.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by pix-i on 03/02/2017.
 * ${Copyright}
 */

@Configuration
public class ConfigTest {

    @Bean
    public PasswordEncoder encoder(){ return new BCryptPasswordEncoder(11);}

}
