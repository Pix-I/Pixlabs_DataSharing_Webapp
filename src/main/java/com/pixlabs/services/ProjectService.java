package com.pixlabs.services;

import com.pixlabs.data.entities.Project;
import com.pixlabs.data.entities.User;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */
public interface ProjectService {

    Project createProject(String title, String description, boolean restricted, User user);



}
