package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.User;
import com.pixlabs.events.NewRegistrationCompleteEvent;
import com.pixlabs.exceptions.UserAlreadyExistException;
import com.pixlabs.services.UserService;
import com.pixlabs.web.dto.UserDto;
import com.pixlabs.web.util.GenericResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Controller
@RequestMapping("/auth")
public class SecurityController {

    private UserService userService;
    private ApplicationEventPublisher eventPublisher;
    private JavaMailSender mailSender;
    private Environment env;


    @RequestMapping(value = "/loginSuccess")
    @ResponseBody
    public GenericResponse login(Principal principal, Model model){
        System.out.println("Logging in");


        if(principal!=null){
            System.out.println(principal);
            return new GenericResponse("success");

        }
        return new GenericResponse("login","invalid");
    }

    @RequestMapping("/tokenConfirm")
    @ResponseBody
    public GenericResponse confirmToken(@RequestParam("token")String token){
        if(userService.validateConfirmationToken(token)){
            return new GenericResponse("valid");
        }

        return new GenericResponse("token","invalid");
    }

    @RequestMapping("/recovery")
    @ResponseBody
    public GenericResponse recoveryEmail(@RequestParam("recoveryEmail")String email,HttpServletRequest request){
        System.out.println("Recovering account, send new email.");
        final User user = userService.findUserByEmail(email);
        if(user==null){
            return new GenericResponse("UserNotFound","notFound");
        }
        final String token = UUID.randomUUID().toString();
        userService.createResetToken(user,token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request),token,user));

        return new GenericResponse("success");
    }




    @RequestMapping("/register")
    @ResponseBody
    public GenericResponse register(@Valid final UserDto userDto, final HttpServletRequest request){
        System.out.println("Registering new user acc:" + userDto.toString());
        try {
            User user = userService.registerAccount(userDto);
            eventPublisher.publishEvent(new NewRegistrationCompleteEvent(user,getAppUrl(request)));
        } catch (UserAlreadyExistException e){
            return new GenericResponse("UserAlreadyExist",e.getMessage());
        }
        //Need to add email verification
        return new GenericResponse("success");
    }




    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Inject
    public void setMailSender(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Inject
    public SecurityController setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        return this;
    }

    // UTILS?


    private String getAppUrl(HttpServletRequest request){
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = "message.resetPassword";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("mail.support.email"));
        return email;
    }

    @Inject
    public void setEnv(Environment env) {
        this.env = env;
    }
}
