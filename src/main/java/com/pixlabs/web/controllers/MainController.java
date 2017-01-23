package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.User;
import com.pixlabs.web.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by pix-i on 31/12/2016.
 * ${Copyright}
 */


@Controller
public class MainController {

    @RequestMapping(value = {"/index","/Index","/"})
    public String index(Principal principal, Model model){
        if(principal==null) {
            UserDto user = new UserDto();
            model.addAttribute("user", user);
            System.out.println("Null principal");
        } else {
            System.out.println("Index:" + principal.toString());
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user",user);
        }
        return "Index";
    }

    @RequestMapping("/Access_Denied")
    public String accessDeniedPage(Model model){
        model.addAttribute("user", getPrincipal());
        return "AccessDenied";
    }

    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


}
