package com.pixlabs.services;

import com.pixlabs.data.entities.Project;
import com.pixlabs.data.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */
public interface ProjectService {

    void createProject(String title, String description, String tags, User user);

    @Transactional
    LinkedList<Project> findProjectsByTag(String tags);

    @Transactional
    void updateTags(Project project, String tags);

    @Transactional
    Project getProjectByTitle(String title);
}
