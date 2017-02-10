package com.pixlabs.services;

import com.pixlabs.data.dao.projects.ProjectRepository;
import com.pixlabs.data.dao.projects.ProjectTagRepository;
import com.pixlabs.data.dao.projects.pldata.DataSetRepository;
import com.pixlabs.data.dao.projects.pldata.DataUnitRepository;
import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.ProjectTag;
import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.projects.pldata.DataUnit;
import com.pixlabs.data.entities.user.User;
import com.pixlabs.exceptions.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by pix-i on 10/02/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectServiceTest {

    private WebApplicationContext applicationContext;

    private UserRepository userRepository;
    private ProjectService projectService;
    private ProjectRepository projectRepository;
    private ProjectTagRepository tagRepository;
    private MockMvc mockMvc;

    private User testUser;
    private User testOtherUser;
    private DataSetRepository dataSetRepository;
    private DataUnitRepository dataUnitRepository;
    private Map<String,Long> dummyDataSet;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        tagRepository.save(new ProjectTag("tag1"));
        tagRepository.flush();
        testUser = new User();
        testUser.setEmail("admin@pix-labs.com");
        testUser.setPassword("testing");
        testUser.setUsername("test");
        userRepository.save(testUser);

        testOtherUser = new User();
        testOtherUser.setEmail("spam@pix-labs.com");
        testOtherUser.setPassword("testing");
        testOtherUser.setUsername("testOther");
        userRepository.save(testOtherUser);

        userRepository.flush();

        createDummyDataSet();
        createDummyProjects();

    }


    private void createDummyDataSet(){
        //Dummy dataset
        dummyDataSet= new HashMap<>();
        dummyDataSet.put("data1",1L);
        dummyDataSet.put("data2",2L);
        dummyDataSet.put("data3",3L);
        projectService.addDataSet(testUser,dummyDataSet,"dummyDataSet");
    }

    private void createDummyProjects(){
        for (int i = 0; i <5 ; i++) {
            Project p = projectService.createProject("dummy_" +i,"Dummy project","dummyTags",testUser);
            projectService.setProjectPublic(testUser,p,true);
        }
    }




    @Test
    public void createProject() throws Exception {
        projectService.createProject("newProject","description","tag",testUser);
        assertNotNull(projectRepository.findByTitle("newProject"));

    }

    @Test
    public void findProjectsByTagSuccess() throws Exception {
        List<Project> projectList = projectService.findProjectsByTag("dummyTags");
        System.out.println(projectList.size());
        assert (projectList.size()==5);
    }

    @Test
    public void findProjectsByTagFail() throws Exception {
        List<Project> projectList = projectService.findProjectsByTag("dummyTag");
        assert (projectList.size()==0);
    }



    @Test
    public void setProjectPublic() throws Exception {
        Project project = projectRepository.findByTitle("dummy_1");
        projectService.setProjectPublic(testUser,project,true);
        assertEquals(false,projectRepository.findByTitle("dummy_1").isRestricted());
    }

    @Test
    public void setProjectPrivate() throws Exception {
        Project project = projectRepository.findByTitle("dummy_1");
        projectService.setProjectPublic(testUser,project,false);
        assertEquals(true,projectRepository.findByTitle("dummy_1").isRestricted());
    }

    @Test
    public void updateTags() throws Exception {
        Project project = projectRepository.findByTitle("dummy_1");
        assertNotNull(project);
        projectService.updateTags(testUser,project,"newTag,newTag2");
        assertNotNull(tagRepository.findByName("newTag"));
        assertNotNull(tagRepository.findByName("newTag2"));
        project = projectRepository.findByTitle("dummy_1");
        assertEquals(2, project.getTagList().size());
    }

    @Test
    public void addDataSetSuccess() throws Exception {
        Map<String,Long> dummyDataSet = new HashMap<>();
        dummyDataSet.put("data1",1L);
        projectService.addDataSet(testUser,dummyDataSet,"testDataSet_1");
        assertNotNull(dataSetRepository.findByName("testDataSet_1"));
    }

    @Test(expected = EntityExistsException.class)
    public void addDataSetFailure() throws Exception {
        Map<String,Long> dummyDataSet = new HashMap<>();
        dummyDataSet.put("data1",1L);
        projectService.addDataSet(testUser,dummyDataSet,"dummyDataSet");
    }


    @Test
    public void voteProjectSuccess() throws Exception {
        Project project = projectRepository.findByTitle("dummy_1");
        int expectedVotes = project.getTotalUpVotes()-1;
        projectService.voteProject(testUser,projectRepository.findByTitle("dummy_1"),-1);
        project = projectRepository.findByTitle("dummy_1");
        assertEquals(expectedVotes,project.getTotalUpVotes());
        assertNotNull(project.getVotes().get(0));
        assertNotNull(testUser.getProjectVotes().get(0));
    }

    @Test(expected = PermissionDeniedException.class)
    public void voteProjectFailure() throws Exception {
        projectService.voteProject(null,projectRepository.findByTitle("dummy_1"),-1);

    }

    @Test
    public void updateDataSet() throws Exception {
        Map<String,Long> newDummyDataSet = new HashMap<>();
        newDummyDataSet.put("data1",1L);
        newDummyDataSet.put("data2",2L);
        newDummyDataSet.put("data3",4L);
        projectService.updateDataSet(testUser,"dummyDataSet",newDummyDataSet);
        DataSet result = dataSetRepository.findByName("dummyDataSet");
        assertEquals(4L, result.getDataList().get(0).getValue());
    }

    @Test
    public void getDataUnit() throws Exception {
        DataSet set = dataSetRepository.findByName("dummyDataSet");
        DataUnit unit = set.getDataList().get(0);
        assertEquals(
        projectService.getDataUnit(testUser,unit.getId()).getValue(),unit.getValue());
    }

    @Test
    public void getDataUnitPermissionSuccess() throws Exception {
        DataSet set = dataSetRepository.findByName("dummyDataSet");
        DataUnit unit = set.getDataList().get(1);
        set.setPublic(false);
        dataSetRepository.save(set);
        projectService.getDataUnit(testUser,unit.getId());
    }


    @Test(expected = PermissionDeniedException.class)
    public void getDataUnitFail() throws Exception {
        DataSet set = dataSetRepository.findByName("dummyDataSet");
        DataUnit unit = set.getDataList().get(0);
        set.setPublic(false);
        dataSetRepository.save(set);
        projectService.getDataUnit(testOtherUser,unit.getId());
    }


    @Test
    public void connectDataSet() throws Exception {
        Project project = projectRepository.findByTitle("dummy_1");
        projectService.connectDataSet(testUser, project, "dummyDataSet");
        User resultUser = userRepository.findByUsername("test");
        assertNotNull(resultUser.getDataSets());
        if(!resultUser.getDataSets().contains(dataSetRepository.findByName("dummyDataSet"))){
            fail();
        }
    }

    @Test
    public void getProjectByTitle() throws Exception {
        projectService.getProjectByTitle(testUser,"dummy_1");
    }


    @Test(expected = PermissionDeniedException.class)
    public void getProjectByTitleFailure() throws Exception {
        projectService.getProjectByTitle(testOtherUser,"dummy_1");
    }



    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
        this.tagRepository = projectTagRepository;
    }

    @Inject
    public void setDataSetRepository(DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
    }

    @Inject
    public void setDataUnitRepository(DataUnitRepository dataUnitRepository) {
        this.dataUnitRepository = dataUnitRepository;
    }



}