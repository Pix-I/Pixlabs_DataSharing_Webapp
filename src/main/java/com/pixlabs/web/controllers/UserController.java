package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.user.User;
import com.pixlabs.services.UserService;
import com.pixlabs.web.dto.UserDto;
import com.pixlabs.web.dto.UserPrefDto;
import com.pixlabs.web.util.GenericResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.ParseException;

/**
 * Created by pix-i on 25/01/2017.
 * ${Copyright}
 */

@Controller
public class UserController {


    private UserService userService;

    @RequestMapping("/preferences")
    public String userPrefs(Model model) {
        Object user =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",(User) user);
            model.addAttribute("userPrefDto",new UserPrefDto());
            return "user/Preferences";
        }
        return "Login";
    }

    @RequestMapping("/updateInfo")
    @ResponseBody
    public GenericResponse savePrefs(Model model, UserPrefDto userPrefDto){
            Object user =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user instanceof User){
                try {
                    userService.updatePrefs(userPrefDto,(User) user);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new GenericResponse("error","invalidDate");
                }
                return new GenericResponse("success");
            }

        return new GenericResponse("error","User isn't connected");
    }


    @RequestMapping("/user/{userId}/")
    public String userView(@PathVariable String userId, Model model){
        final User userInfo = userService.findUserByUsername(userId);
        model.addAttribute("userInfo",userInfo);
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",user);
        }
        else{
            model.addAttribute("newUser",new UserDto());}
        return "user/UserView";
    }

    @RequestMapping("/user/online")
    public String getOnlineUsers(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",user);
        }
        else{
            model.addAttribute("newUser",new UserDto());
        }
        model.addAttribute("onlineUsers",userService.getUsersFromRegistry());
        return "OnlineUsers";
    }


    @RequestMapping("/user/{userId}/profilePicture")
    @ResponseBody
    public byte[] userPicture(@PathVariable String userId, Model model){
        final User user = userService.findUserByUsername(userId);
        return user.getProfilePicture();
    }


    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
