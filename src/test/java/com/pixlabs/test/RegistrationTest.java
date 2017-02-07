package com.pixlabs.test;


import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.dao.user.VerificationTokenRepository;
import com.pixlabs.data.entities.user.User;
import com.pixlabs.data.entities.user.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by pix-i on 03/02/2017.
 * ${Copyright}
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RegistrationTest {

    private WebApplicationContext applicationContext;

    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;

    private MockMvc mockMvc;
    private String token;



    //Setting up a new user
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();

        User user = new User();
        user.setEmail(UUID.randomUUID().toString()+"@pix-labs.com");
        user.setPassword(UUID.randomUUID().toString());
        user.setUsername("test");

        userRepository.save(user);
        token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
        verificationTokenRepository.flush();
    }

    @Test
    public void testRegistrationConfirm() throws Exception{
        assertNotNull(userRepository.findByUsername("test"));
        ResultActions resultActions = this.mockMvc.perform(get("/auth/tokenConfirm?token=" + token));
        if(!userRepository.findByUsername("test").isEnabled()){
            fail();
        }
    }




    @Inject
    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Inject
    public void setVerificationTokenRepository(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }
}
