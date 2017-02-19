package com.pixlabs.services;

import com.pixlabs.data.dao.projects.ProjectRepository;
import com.pixlabs.data.dao.projects.ProjectTagRepository;
import com.pixlabs.data.dao.projects.ProjectVotesRepository;
import com.pixlabs.data.dao.projects.pldata.DataSetRepository;
import com.pixlabs.data.dao.projects.pldata.DataSetVotesRepository;
import com.pixlabs.data.dao.projects.pldata.DataUnitRepository;
import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.ProjectTag;
import com.pixlabs.data.entities.projects.ProjectVote;
import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.projects.pldata.DataUnit;
import com.pixlabs.data.entities.user.User;
import com.pixlabs.exceptions.PermissionDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectTagRepository projectTagRepository;


    private DataSetRepository dataSetRepository;
    private DataUnitRepository dataUnitRepository;

    private ProjectVotesRepository projectVotesRepository;
    private DataSetVotesRepository dataSetVotesRepository;


    /**
     * Creates a new project and links it to the given user.
     *
     * @param title       Project title, will be used to access the project.
     * @param description Project description
     * @param tags        Tags will be used to find projects of the same kind.
     * @param user        Owner of the project.
     */

    @Override
    @Transactional
    public Project createProject(String title, String description, String tags, User user) {
        final Project project =
                new Project();
        project.setTitle(title);
        project.setDescription(description);
        //Tags
        SortedSet<ProjectTag> tagList = tagsParser(tags);
        project.setTagList(tagList);
        projectRepository.save(project);
        user.addProject(project);
        userRepository.save(user);
        for (ProjectTag tag : tagList) {
            tag.addProject(project);
            projectTagRepository.save(tag);
        }
        return project;
    }


    private SortedSet<ProjectTag> tagsParser(String s) {
        s = s.replaceAll("\\s", "");
        String[] tags = s.split(",");
        SortedSet<ProjectTag> tagList = new TreeSet<>();
        for (String t : tags) {
            ProjectTag tempTag = projectTagRepository.findByName(t);
            if (tempTag == null) {
                tempTag = new ProjectTag(t);
            }
            tagList.add(tempTag);
        }
        return tagList;
    }


    /**
     * Finds all the projects containing the following tags
     *
     * @param s Tag
     * @return LinkedList of projects containing the tag.
     */
    @Override
    @Transactional
    public LinkedList<Project> findProjectsByTag(String s) {
        s = s.replaceAll("\\s", "");
        String[] tags = s.split(",");
        LinkedList<Project> projectList = new LinkedList<>();
        for (String t : tags) {
            ProjectTag tag = projectTagRepository.findByName(t);
            if (tag != null) {
                for (Project p : tag.getProjects()) {
                    if (!projectList.contains(p) && !p.isRestricted()) {
                        projectList.add(p);
                    }
                }

            }
        }
        return projectList;
    }

    private void hasPermission(User user, Project project) {
        //TODO: add check to see if it's an admin
        if (!project.getOwner().equals(user))
            throw new PermissionDeniedException();
    }


    @Override
    @Transactional
    public void setProjectPublic(User user, Project project, boolean flag) {
        hasPermission(user, project);
        project.setRestricted(!flag);
        projectRepository.save(project);
    }

    /**
     * Updates the tags for a project.
     *
     * @param user    Must be the owner of the project.
     * @param project Project that needs to update its tags.
     * @param tags    New tags.
     */
    @Override
    @Transactional
    public void updateTags(User user, Project project, String tags) {
        hasPermission(user, project);
        SortedSet<ProjectTag> tagList = tagsParser(tags);
        for (ProjectTag tag : tagList) {
            if (!tag.getProjects().contains(project)) {
                tag.addProject(project);
                projectTagRepository.save(tag);
            }
        }
        for (ProjectTag tag : project.getTagList()) {
            if (!tagList.contains(tag)) {
                tag.removeProject(project);
                projectTagRepository.save(tag);
            }
        }
        project.setTagList(tagList);
        projectRepository.save(project);

    }


    /**
     * Create and saves a dataset consisting of mutliple dataunits.
     *
     * @param owner   The owner of the dataset, he'll be able to change the properties of the set.
     * @param dataMap The dataunits in a map form
     * @param name    The name of the dataset.
     */
    @Override
    @Transactional
    public void addDataSet(User owner, Map<String, Long> dataMap, String name) {
        if(dataSetRepository.findByName(name)!=null)
            throw new EntityExistsException("Entity already exists.");
        if(owner==null)
            throw new PermissionDeniedException("User is not logged in");
        DataSet dataSet = new DataSet();
        dataSet.setDataSetOwner(owner);
        dataSet.setName(name);
        dataSet.setOpen(true);
        dataSet.setPublic(false);
        for (Map.Entry<String, Long> entry : dataMap.entrySet()) {
            DataUnit unit = new DataUnit(dataSet, entry.getKey(), entry.getValue());
            dataSet.addDataUnit(unit);
            dataUnitRepository.save(unit);
        }
        owner.addDataset(dataSet);
        dataSetRepository.save(dataSet);
        userRepository.save(owner);
    }

    //TODO Add check to see if it's archived or not and also only write if it's a new one...
    /**
     * Changes the upvotes of a project.
     *
     * @param activeUser User that votes.
     * @param project    Affected project.
     * @param value      Int value can be -1,0 or 1, represents the vote.
     */
    @Override
    @Transactional
    public void voteProject(User activeUser, Project project, int value) {
        if(activeUser==null)
            throw new PermissionDeniedException("User not logged in");
        if(!project.isRestricted()) {
            ProjectVote projectVote = project.updateVote(activeUser,value);
            projectVotesRepository.save(projectVote);
            projectRepository.save(project);
            userRepository.save(activeUser.addProjectVote(projectVote));
        }
    }


    /**
     * Updates a DataSet with new values, it cannot add new keys.
     *
     * @param activeUser The user that wants to update the values
     * @param name       Name of the DataSet to be updated.
     * @param dataMap    Map of the values that'll replace the previous values.
     */
    @Override
    @Transactional
    public void updateDataSet(User activeUser, String name, Map<String, Long> dataMap) {
        DataSet dataSet = dataSetRepository.findByName(name);
        if (dataSet != null) {
            if (dataSet.isPublic() || dataSet.getDataSetOwner().equals(activeUser)) {
                for (DataUnit dataUnit : dataSet.getDataList()) {
                    if (dataMap.containsKey(dataUnit.getName())) {
                        dataUnit.setValue(dataMap.get(dataUnit.getName()));
                        dataUnitRepository.save(dataUnit);
                    }
                }
                return;
            }
            throw new PermissionDeniedException("DataSet not public");
        }
        throw new EntityNotFoundException("DataSet not found:" + name);
    }

    @Override
    @Transactional
    public DataUnit getDataUnit(User user, long id) {
        DataUnit dataUnit = dataUnitRepository.findById(id);
        if (dataUnit != null) {
            DataSet dataSet = dataUnit.getOwner();
            if (dataSet.isPublic() || dataSet.getDataSetOwner().equals(user)) {
                return dataUnit;
            }
            throw new PermissionDeniedException("Data isn't public");
        }
        throw new EntityNotFoundException("DataUnit not found, id:" + id);
    }


    /**
     * Creates and connects a DataSet to a project.
     *
     * @param activeUser Owner to be of the DataSet.
     * @param project    Project where the DataSet belongs.
     * @param dataMap    Map of the data.
     * @param name       Name of the DataSet.
     */

    @Override
    @Transactional
    public void createAndConnectDataSet(User activeUser, Project project, Map<String, Long> dataMap, String name) {
        addDataSet(activeUser, dataMap, name);
        connectDataSet(activeUser, project, name);
    }

    /**
     * Connects a DataSet to a project if the DataSet is public or the activeUser owns the DataSet.
     *
     * @param project     The project that wants the DataSet.
     * @param dataSetName Name of the dataSet to be connected.
     * @param activeUser  User asking to connect the set.
     */
    @Override
    @Transactional
    public boolean connectDataSet(User activeUser, Project project, String dataSetName) {
        DataSet dataSet = dataSetRepository.findByName(dataSetName);
        if (dataSet != null && (dataSet.isPublic() || dataSet.getDataSetOwner().equals(activeUser))) {
            project.addDataSet(dataSet);
            dataSet.addProject(project);
            projectRepository.save(project);
            dataSetRepository.save(dataSet);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Project getProjectByTitle(User activeUser,String title) {
        Project project = projectRepository.findByTitle(title);
        hasPermission(activeUser,project);
        return project;
    }

    @Override
    @Transactional
    public DataSet getDataSet(User user, String dataSetName){
        DataSet dataSet = dataSetRepository.findByName(dataSetName);
        if(dataSet==null)
            throw new EntityNotFoundException("There's no existing dataset with that name.");
        if(!dataSet.isPublic()||!dataSet.getDataSetOwner().equals(user))
            throw new PermissionDeniedException("The user doesn't have the right to access this set");
        return dataSet;
    }

    @Override
    @Transactional
    public void setDataSetPublic(User user,DataSet dataSet,boolean flag){
        if(!dataSet.isPublic()||!dataSet.getDataSetOwner().equals(user))
            throw new PermissionDeniedException("The user doesn't have the right to access this set");
        dataSet.setPublic(flag);
        dataSetRepository.save(dataSet);
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

    @Inject
    public void setDataSetRepository(DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
    }

    @Inject
    public void setDataUnitRepository(DataUnitRepository dataUnitRepository) {
        this.dataUnitRepository = dataUnitRepository;
    }

    @Inject
    public void setDataSetVotesRepository(DataSetVotesRepository dataSetVotesRepository) {
        this.dataSetVotesRepository = dataSetVotesRepository;
    }

    @Inject
    public void setProjectVotesRepository(ProjectVotesRepository projectVotesRepository) {
        this.projectVotesRepository = projectVotesRepository;
    }
}
