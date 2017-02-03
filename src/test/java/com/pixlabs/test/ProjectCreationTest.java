package com.pixlabs.test;

import com.pixlabs.data.dao.ProjectRepository;
import com.pixlabs.data.dao.ProjectTagRepository;
import com.pixlabs.data.dao.UserRepository;
import com.pixlabs.data.entities.ProjectTag;
import com.pixlabs.data.entities.User;
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

import static org.junit.Assert.assertNotNull;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectCreationTest {


    private WebApplicationContext applicationContext;

    private UserRepository userRepository;
    private ProjectService projectService;
    private ProjectRepository projectRepository;
    private ProjectTagRepository tagRepository;

    private MockMvc mockMvc;

    public ProjectCreationTest() {
    }

    //Setting up a new user
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();

        tagRepository.save(new ProjectTag("tag1"));
        tagRepository.flush();
        User user = new User();
        user.setEmail("admin@pix-labs.com");
        user.setPassword("testing");
        user.setUsername("test");
        userRepository.save(user);
        userRepository.flush();
    }

    @Test
    public void projectCreationTest() throws Exception{
        final User user = userRepository.findByUsername("test");
        projectService.createProject("testProject","test description","tag",user);

        assertNotNull(projectRepository.findBytitle("testProject"));
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


    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Inject
    public void setTagRepository(ProjectTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Inject
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
}
