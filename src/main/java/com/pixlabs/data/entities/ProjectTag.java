package com.pixlabs.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */

@Entity
public class ProjectTag {


    @GeneratedValue
    @Id
    private long id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "tagList")
    private List<Project> projects;

    public ProjectTag(){}

    public ProjectTag(String tagName){
        this.name = tagName;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
