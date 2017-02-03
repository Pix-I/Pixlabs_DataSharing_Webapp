package com.pixlabs.web.controllers;

import com.pixlabs.data.entities.User;
import com.pixlabs.services.ProjectService;
import com.pixlabs.web.dto.ProjectDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by pix-i on 03/02/2017.
 * ${Copyright}
 */
@Controller
public class ProjectController {

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    private ProjectService projectService;



    @RequestMapping(value = "/createNewProject")
    public String createNewProject(Model model){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            model.addAttribute("activeUser",user);
        }
        model.addAttribute("projectDto",new ProjectDto());
        return "project/ProjectCreation";
    }

    @RequestMapping(value = "/createProject")
    public String createProject(Model model, @Valid ProjectDto projectDto, BindingResult bindingResult){
        System.out.println(projectDto.getTitle());
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User) {
            model.addAttribute("activeUser", user);
        }
        if(bindingResult.hasErrors()){

            return "project/ProjectCreation";
        }
        projectService.createProject(projectDto.getTitle(), projectDto.getDescription(),projectDto.getTags(), (User) user);


        return "project/Project";
    }

}
