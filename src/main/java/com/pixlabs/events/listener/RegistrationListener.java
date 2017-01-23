package com.pixlabs.events.listener;

import com.pixlabs.data.entities.User;
import com.pixlabs.events.NewRegistrationCompleteEvent;
import com.pixlabs.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Created by pix-i on 20/01/2017.
 * ${Copyright}
 */

@Component
public class RegistrationListener implements ApplicationListener<NewRegistrationCompleteEvent>{


    private JavaMailSender mailSender;

    private UserService userService;

    @Value("${mail.support.email}")
    private String registrationEmail;



    private SimpleMailMessage registrationMail(final String toEmail,String token){
        System.out.println("Sending mail to:" + toEmail);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject("Registration on Pix-labs");
        email.setText("Welcome to Pix-labs! Please don't reply to this email.\n"
                + token);
        email.setFrom(registrationEmail);
        return email;
    }



    @Inject
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }





    @Override
    public void onApplicationEvent(NewRegistrationCompleteEvent event) {
        System.out.println("New user registered!");
        final User user = event.getUser();
        final String token =  UUID.randomUUID().toString();
        final SimpleMailMessage email = registrationMail(user.getEmail(),event.getAppUrl() + "/auth/tokenConfirm?token="+token);
        userService.createVerificationToken(user,token);
        mailSender.send(email);
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
