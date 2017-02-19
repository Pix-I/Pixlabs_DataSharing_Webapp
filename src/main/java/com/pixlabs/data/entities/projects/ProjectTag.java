package com.pixlabs.data.entities.projects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */

@Entity
public class ProjectTag implements Comparable{


    @GeneratedValue
    @Id
    private long id;

    public String getName() {
        return name;
    }

    @NotNull
    private String name;

    @Override
    public String toString() {
        return "ProjectTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        return o == this || o != null
                && getClass() == o.getClass()
                && o instanceof ProjectTag
                && ((ProjectTag) o).getId() == this.id
                && ((ProjectTag) o).getName().equals(this.name);
    }

    @ManyToMany(mappedBy = "tagList",fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();


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

    public void addProject(Project project) {
        if(this.projects==null){
            projects = new LinkedList<>();
        }
        this.projects.add(project);
    }

    public void removeProject(Project project){
        projects.removeIf(p -> p.equals(project));
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof ProjectTag){
            ProjectTag tag = (ProjectTag) o;
            return tag.getName().compareTo(this.getName());
        }
        return 0;
    }
}
