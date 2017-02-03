package com.pixlabs.services;

import com.pixlabs.data.dao.ProjectRepository;
import com.pixlabs.data.dao.ProjectTagRepository;
import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.entities.Project;
import com.pixlabs.data.entities.ProjectTag;
import com.pixlabs.data.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectTagRepository projectTagRepository;

    /**
     * Creates a new project and links it to the given user.
     * @param title Project title, will be used to access the project.
     * @param description Project description
     * @param tags Tags will be used to find projects of the same kind.
     * @param user Owner of the project.
     */

    @Override
    @Transactional
    public void createProject(String title, String description, String tags, User user) {
        final Project project =
                new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setTagList(tagsParser(tags));
        projectRepository.save(project);
        user.addProject(project);
        userRepository.save(user);
    }


    private List<ProjectTag > tagsParser(String s){
        s = s.replaceAll("\\s","");
        String[] tags =s.split(",");
        List<ProjectTag> tagList = new LinkedList<>();
        for(String t:tags){
            ProjectTag tempTag = projectTagRepository.findByName(t);
            if(tempTag==null){
                tempTag = new ProjectTag(t);
                projectTagRepository.save(tempTag);
            }
            tagList.add(tempTag);
        }
        return tagList;
    }


    @Inject
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Inject
    public void setProjectTagRepository(ProjectTagRepository projectTagRepository) {
        this.projectTagRepository = projectTagRepository;
    }
}
