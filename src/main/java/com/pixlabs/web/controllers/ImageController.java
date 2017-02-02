package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.User;
import com.pixlabs.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Created by pix-i on 25/01/2017.
 * ${Copyright}
 */

@Controller
@RequestMapping("/img")
public class ImageController {

    private UserService userService;


    @RequestMapping("/profilePicture")
    @ResponseBody
    public byte[] getProfilePicture(){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            return ((User) user).getProfilePicture();
        }
        return null;

    }


    //Need to add default picture I guess
    @RequestMapping("/{userId}/profilePicture")
    @ResponseBody
    public byte[] getUserProfilePicture(@PathVariable String userId){
        final User user = userService.findUserByUsername(userId);
        if(user!=null){
            return user.getProfilePicture();
        }
        else
            return null;
    }


    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
