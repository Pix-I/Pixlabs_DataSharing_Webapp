package com.pixlabs.data.dao;

import com.pixlabs.data.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findBytitle(String title);

    @Override
    void delete(Project project);
}
