package com.pixlabs.data.entities.projects;

import com.pixlabs.data.entities.user.User;

import javax.persistence.*;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */

@Entity
public class ProjectVotes {

    @GeneratedValue
    @Id
    private long id;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "projectVotes")
    private User user;

    private int vote;

    private Project project;

    public ProjectVotes() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
