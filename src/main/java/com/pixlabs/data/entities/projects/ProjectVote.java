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
public class ProjectVote {

    @GeneratedValue
    @Id
    private long id;

    /**
     * Should be @ManyToOne, as a user can have multiple ProjectVote
     * and a Project is linked to all the ProjectVote for that Project.
     * //TODO Maybe add a "recent history" to user.
     */
    @ManyToOne
    private User user;

    /**
     * Value is either -1,0 or 1. It represents the up or down vote.
     */
    private int vote;

    /**
     * Should be @ManyToOne, projects have multiple ProjectVote, but ProjectVote only
     * have one Project.
     */
    @ManyToOne
    private Project project;

    /**
     * Empty constructor to make JPA happy
     * @param activeUser
     * @param project
     * @param value
     */
    public ProjectVote(User activeUser, Project project, int value) {
    }

    /**
     * Just a constructor, links some User and Project to a vote.
     * @param user User that voted.
     * @param vote int value between -1,1 to represent the vote.
     * @param project The project that was voted for.
     */
    public ProjectVote(User user, int vote, Project project) {
        this.user = user;
        this.vote = vote;
        this.project = project;
    }


    @Override
    public String toString() {
        return "ProjectVote{" +
                "id=" + id +
                ", user=" + user +
                ", vote=" + vote +
                ", project=" + project +
                '}';
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
