package com.pixlabs.services;

import com.pixlabs.data.entities.Project;
import com.pixlabs.data.entities.User;
import org.springframework.stereotype.Service;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    @Override
    public Project createProject(String title, String description, boolean restricted,User user) {
        return new Project.Builder().title(title).description(description).restricted(restricted).user(user).build();
    }
}
