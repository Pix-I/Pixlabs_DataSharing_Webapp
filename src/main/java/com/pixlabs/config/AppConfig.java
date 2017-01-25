package com.pixlabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by pix-i on 23/01/2017.
 * ${Copyright}
 */

@Configuration
//@PropertySource()
public class AppConfig {

    @Inject
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Environment environment;



    //

    /**
     * A simple implementation of JavaMailSender, MailSender bean worked perfectly but the "warning" from Intellij annoyed me.
     * @return An implementation of JavaMailSender
     */
    @Bean
    public JavaMailSenderImpl javaMailSender(){
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        try{
            mailSenderImpl.setUsername(environment.getRequiredProperty("mail.username"));
            mailSenderImpl.setPassword(environment.getRequiredProperty("mail.password"));
            mailSenderImpl.setPort( environment.getRequiredProperty("mail.port",Integer.class));
            mailSenderImpl.setProtocol(environment.getRequiredProperty("mail.protocol"));
            mailSenderImpl.setHost(environment.getRequiredProperty("mail.host"));
        } catch (IllegalStateException e){
            System.out.println("Could not resolve email properties.");
            throw e;
        }
        final Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.socketFactory.port", 25);
        mailProperties.put("mail.transport.protocol", "smtps");
        mailProperties.put("mail.smtps.quitwait", false);

        mailSenderImpl.setJavaMailProperties(mailProperties);
        return mailSenderImpl;
    }

}
