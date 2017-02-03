package com.pixlabs.data.dao;

import com.pixlabs.data.entities.ProjectTag;
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
