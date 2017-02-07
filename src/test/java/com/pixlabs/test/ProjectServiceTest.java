package com.pixlabs.test;

import com.pixlabs.data.dao.projects.ProjectRepository;
import com.pixlabs.data.dao.projects.ProjectTagRepository;
import com.pixlabs.data.dao.projects.pldata.DataSetRepository;
import com.pixlabs.data.dao.projects.pldata.DataUnitRepository;
import com.pixlabs.data.dao.user.UserRepository;
import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.ProjectTag;
import com.pixlabs.data.entities.user.User;
import com.pixlabs.services.ProjectService;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
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
    private DataSetRepository dataSetRepository;
    private DataUnitRepository dataUnitRepository;

    public ProjectServiceTest() {
    }

    //Setting up a new user
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();

        tagRepository.save(new ProjectTag("tag1"));
        tagRepository.flush();
        testUser = new User();
        testUser.setEmail("admin@pix-labs.com");
        testUser.setPassword("testing");
        testUser.setUsername("test");
        userRepository.save(testUser);
        userRepository.flush();

        //Dummy dataset
        Map<String,Long> dummyDataSet = new HashMap<>();
        dummyDataSet.put("data1",1L);
        dummyDataSet.put("data2",2L);
        dummyDataSet.put("data3",4L);
        projectService.addDataSet(testUser,dummyDataSet,"testDataSet");

        System.out.println("Adding dummy projects");
        projectService.createProject("dummyProject","test description","tag",testUser);
        projectService.createProject("tagsTestProject1","test description","tag1",testUser);
        projectService.createProject("tagsTestProject2","test description","tag2,tag4",testUser);
        projectService.createProject("tagsTestProject3","test description","tag2",testUser);

    }

    @Test
    public void projectCreationTest() throws Exception{
        final User user = userRepository.findByUsername("test");
        projectService.createProject("testProject","test description","tag",user);

        assertNotNull(projectRepository.findByTitle("testProject"));
        assertNotNull(tagRepository.findByName("tag"));
    }

    @Test
    public void tagsAdditionTest() throws Exception{
        assertNotNull(tagRepository.findByName("tag1"));
        final User user = userRepository.findByUsername("test");
        String tags = "tag1,tag2 ,  tag3 ,  tag4";
        projectService.createProject("tagsTestProject","test description",tags,user);
        assertNotNull(tagRepository.findByName("tag3"));
        assertNotNull(tagRepository.findByName("tag2"));
        assertNotNull(tagRepository.findByName("tag1"));
    }

    @Test
    public void findByTagsTest(){
        String tags = "tag1 , newTag ,tag4,tag2";
        projectService.createProject("tagsTestProject3","test description","unfoundTag",testUser);
        LinkedList<Project> projects = projectService.findProjectsByTag(tags);
        assertNotNull(projects);
        if(projects.size()!=3){
            fail();
        }
    }

    @Test
    public void updateTagsTest(){
        projectService.createProject("testProjectUpdateTags","test description","tag1,tag2,tag3",testUser);
        Project project = projectService.getProjectByTitle("testProjectUpdateTags");
        projectService.updateTags(project,"tag1,tag3");
        if(projectService.getProjectByTitle("testProjectUpdateTags").getTagList().size()!=2){
            System.out.println(projectService.getProjectByTitle("testProjectUpdateTags").getTagList().size());
            fail();
        }

    }



    @Test
    public void connectDataSetTest(){
        Project project = projectRepository.findByTitle("dummyProject");
        projectService.connectDataSet(project,"testDataSet",testUser);
        User resultUser = userRepository.findByUsername("test");
        assertNotNull(resultUser.getDataSets());
        if(!resultUser.getDataSets().contains(dataSetRepository.findByName("testDataSet"))){
            fail();
        }

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
