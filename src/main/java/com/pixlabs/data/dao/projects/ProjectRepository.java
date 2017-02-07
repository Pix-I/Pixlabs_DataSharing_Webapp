package com.pixlabs.data.dao.projects;

import com.pixlabs.data.entities.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findByTitle(String title);

    @Override
    void delete(Project project);
}
