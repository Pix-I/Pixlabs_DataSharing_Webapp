package com.pixlabs.data.dao.projects;

import com.pixlabs.data.entities.projects.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */
public interface ProjectTagRepository extends JpaRepository<ProjectTag,Long> {

    ProjectTag findByName(String name);

    @Override
    void delete(ProjectTag tag);

}
