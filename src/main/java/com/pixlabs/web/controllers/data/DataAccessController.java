package com.pixlabs.web.controllers.data;

import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.projects.pldata.DataUnit;
import com.pixlabs.data.entities.user.User;
import com.pixlabs.exceptions.PermissionDeniedException;
import com.pixlabs.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by pix-i on 10/02/2017.
 */

/**
 * Main role of this controller is to access data, not visualise it.
 */

@Controller
public class DataAccessController {

    private ProjectService projectService;


    @ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "Permission Denied")
    @ExceptionHandler(PermissionDeniedException.class)
    public String permissionDenied(){
        return "Login";
    }

    @ResponseBody
    @RequestMapping("/data/unit/{unitId}")
    public DataUnit getDataUnit(@PathVariable Long unitId){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            return projectService.getDataUnit((User) user,unitId);
        }
        return null;
    }



    @ResponseBody
    @RequestMapping("/data/set/{dataSetName}")
    public DataSet getDataSet(@PathVariable String dataSetName){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof User){
            return projectService.getDataSet((User) user,dataSetName);
        }
        return null;
    }






    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
