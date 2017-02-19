package com.pixlabs.data.entities.projects.pldata;

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
public class DataSetVote {    @GeneratedValue
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
     * Constructor for JPA
     */
    public DataSetVote(){}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Should be @ManyToOne, projects have multiple ProjectVote, but ProjectVote only
     * have one Project.
     */
    @ManyToOne
    private DataSet dataSet;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object o){
        if(o instanceof DataSetVote){
            DataSetVote v = (DataSetVote) o;
            return v.getUser().equals(user) && v.getDataSet().equals(dataSet);
        }
        return false;
    }

}
