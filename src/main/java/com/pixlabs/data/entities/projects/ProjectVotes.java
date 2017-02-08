package com.pixlabs.data.entities.projects;

import com.pixlabs.data.entities.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */

@Entity
public class ProjectVotes {

    @GeneratedValue
    @Id
    private long id;

    /**
     * Should be @ManyToOne, as a user can have multiple ProjectVotes
     * and a Project is linked to all the ProjectVotes for that Project.
     * //TODO Maybe add a "recent history" to user.
     */
    @ManyToOne
    private User user;

    /**
     * Value is either -1,0 or 1. It represents the up or down vote.
     */
    private int vote;

    /**
     * Should be @ManyToOne, projects have multiple ProjectVotes, but ProjectVotes only
     * have one Project.
     */
    @ManyToOne
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
