package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.User;
import com.pixlabs.events.NewRegistrationCompleteEvent;
import com.pixlabs.exceptions.UserAlreadyExistException;
import com.pixlabs.services.SecurityService;
import com.pixlabs.services.UserService;
import com.pixlabs.web.dto.PasswordResetDto;
import com.pixlabs.web.dto.UserDto;
import com.pixlabs.web.util.GenericResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private SecurityService securityService;
    private ApplicationEventPublisher eventPublisher;
    private JavaMailSender mailSender;
    private Environment env;

    @RequestMapping(value = "/loginPage",method = RequestMethod.GET)
    public String loginPage(Model model){
        return "Login";
    }

    @RequestMapping(value = "login",method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse login(Model model,String error, String logout){
        if(error!=null){
            return new GenericResponse("error","invalid");
        }
        return new GenericResponse("success");
    }


    @RequestMapping(value = "/passwordReset",method = RequestMethod.GET)
    public String passwordRecovery(@RequestParam String recoveryToken,@RequestParam long id,Model model){
        System.out.println("Getting page");
        String result = securityService.validatePasswordResetToken(id,recoveryToken);
        if(result!=null) {
            model.addAttribute("passwordResetDto",new PasswordResetDto());
            model.addAttribute("passwordResetUsername", result);
            return "PasswordRecovery";
        }
        return "404";
    }


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
    public String confirmToken(@RequestParam("token")String token){
        if(userService.validateConfirmationToken(token)){
            return "RegistrationComplete";
        }

        return "404";
    }

    @RequestMapping("/recovery")
    @ResponseBody
    public GenericResponse recoveryEmail(@RequestParam("recoveryEmail")String email,HttpServletRequest request){
        final User user = userService.findUserByEmail(email);
        if(user==null){
            return new GenericResponse("UserNotFound","notFound");
        }
        final String token = UUID.randomUUID().toString();
        userService.createResetToken(user,token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request),token,user));

        return new GenericResponse("success");
    }


    @RequestMapping(value = "/savePasswordReset",method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse passwordRecoveryValidation(@Valid PasswordResetDto resetDto, Model model){
        System.out.println("Changing pass:");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserPassword((User) principal,resetDto.getPassword());
        model.addAttribute("activeUser",(User) principal);
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
        final String url = contextPath + "/auth/passwordReset?id=" + user.getId()+ "&recoveryToken=" + token;
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

    @Inject
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
