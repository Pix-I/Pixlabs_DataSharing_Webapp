package com.pixlabs.services;

import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.pldata.DataUnit;
import com.pixlabs.data.entities.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Map;

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
    void addDataSet(User owner, Map<String, Long> dataMap, String name);

    @Transactional
    void updateDataSet(User activeUser, String name, Map<String, Long> dataMap);

    @Transactional
    DataUnit getDataUnit(User user, long id);

    @Transactional
    void createAndConnectDataSet(User activeUser, Project project, Map<String, Long> dataMap, String name);

    @Transactional
    boolean connectDataSet(Project project, String dataSetName,User activeUser);

    @Transactional
    Project getProjectByTitle(String title);
}
