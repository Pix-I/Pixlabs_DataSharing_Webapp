package com.pixlabs.services;

import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.pldata.DataSet;
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

    Project createProject(String title, String description, String tags, User user);

    @Transactional
    LinkedList<Project> findProjectsByTag(String tags);

    @Transactional
    void setProjectPublic(User user, Project project, boolean flag);

    @Transactional
    void updateTags(User user, Project project, String tags);

    @Transactional
    void addDataSet(User owner, Map<String, Long> dataMap, String name);

    @Transactional
    void voteProject(User activeUser, Project project, int value);

    @Transactional
    void updateDataSet(User activeUser, String name, Map<String, Long> dataMap);

    @Transactional
    DataUnit getDataUnit(User user, long id);

    @Transactional
    void createAndConnectDataSet(User activeUser, Project project, Map<String, Long> dataMap, String name);

    @Transactional
    boolean connectDataSet(User activeUser, Project project, String dataSetName);

    @Transactional
    Project getProjectByTitle(User testOtherUser, String title);

    @Transactional
    DataSet getDataSet(User user, String dataSetName);

    @Transactional
    void setDataSetPublic(User user, DataSet dataSet, boolean flag);
}
