package com.pixlabs.data.dao.projects;

import com.pixlabs.data.entities.projects.ProjectVotes;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */
public interface ProjectVotesRepository extends JpaRepository<ProjectVotes,Long> {


     @Override
     void delete(ProjectVotes projectVotes);

}
