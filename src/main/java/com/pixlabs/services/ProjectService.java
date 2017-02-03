package com.pixlabs.services;

import com.pixlabs.data.entities.User;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */
public interface ProjectService {

    void createProject(String title, String description, String tags, User user);
}
