package com.pixlabs.data.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


@Entity
@Table(name = "project")
public class Project {

    @GeneratedValue
    @Id
    private long id;

    @ManyToOne
    private User owner;

    public User getOwner() {
        return owner;
    }

    public String getTags() {
        return tags;
    }

    private String description;

    @NotNull
    private String title;
    private String tags = "";

    @ManyToMany
    @JoinTable(name = "project_tags",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "projectTag_id", referencedColumnName = "id"))
    private List<ProjectTag> tagList;

    private Date creationDate;

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    private boolean restricted;

    public Project(){
        this.restricted = true;
        this.creationDate = new Date();
    }


    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUsers(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project project = (Project) obj;
        if (!project.title.equals(this.title)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", users=" + owner +
                ", title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                ", creationDate=" + creationDate +
                ", restricted=" + restricted +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<ProjectTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<ProjectTag> tagList) {
        this.tagList = tagList;
    }
}
