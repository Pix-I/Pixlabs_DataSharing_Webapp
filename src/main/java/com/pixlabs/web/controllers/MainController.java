package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.user.User;
import com.pixlabs.web.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pix-i on 31/12/2016.
 * ${Copyright}
 */


@Controller
public class MainController {

    @RequestMapping(value = {"/index","/Index","/"})
    public String index(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",(User) user);
        }
        else{
            model.addAttribute("newUser",new UserDto());}
        return "Index";
    }

    @RequestMapping("/Access_Denied")
    public String accessDeniedPage(Model model){
        System.out.println("Access was denied");
        return "AccessDenied";
    }




    @RequestMapping("/header")
    public String header(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",user);
            System.out.println("User entity");
        }
        else{
            System.out.println(user.toString());
            model.addAttribute("user",new UserDto());}
        return "header/Header";
    }




}
