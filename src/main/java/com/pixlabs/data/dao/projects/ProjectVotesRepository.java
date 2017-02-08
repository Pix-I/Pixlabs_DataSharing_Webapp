package com.pixlabs.data.dao.projects;

import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.ProjectVotes;
import com.pixlabs.data.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */
public interface ProjectVotesRepository extends JpaRepository<ProjectVotes,Long>{

     ProjectVotes findByUser(User user);
     ProjectVotes findByProject(Project project);

     @Override
     void delete(ProjectVotes projectVotes);

}
