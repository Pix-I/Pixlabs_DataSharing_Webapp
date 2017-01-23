package com.pixlabs.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Date;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


@Entity
public class Project {

    @GeneratedValue
    @Id
    private long id;

    @ManyToMany(mappedBy = "projects")
    private Collection<User> ownership;

    private String description;

    private String title;

    private Date creationDate;

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    private boolean restricted;

    protected Project(){}

    private Project(Builder builder){
        this.description = builder.description;
        this.title = builder.title;
        this.creationDate = builder.creationDate;
        this.restricted = builder.restricted;
        this.ownership.add(builder.user);
    }

    public static class Builder{

        private String description = "Project description";

        private String title = "";

        private Date creationDate = new Date();

        private boolean restricted = false;
        private User user;

        public Builder(){
        }

        public Project build(){
            return new Project(this);
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder restricted(boolean restricted) {
            this.restricted = restricted;
            return this;
        }


        public Builder user(User user) {
            this.user = user;
            return this;
        }
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
}
