package com.pixlabs.data.dao.projects;

import com.pixlabs.data.entities.projects.ProjectVote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */
public interface ProjectVotesRepository extends JpaRepository<ProjectVote,Long> {


     @Override
     void delete(ProjectVote projectVote);

}
