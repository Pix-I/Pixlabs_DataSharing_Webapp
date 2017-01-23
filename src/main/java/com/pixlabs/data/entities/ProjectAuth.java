package com.pixlabs.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */

@Entity
public class ProjectAuth {


    @Id
    private long id;

    @Column(name ="username")
    private String username;
    @Column(name = "projectId")
    private Long projectId;

    protected ProjectAuth(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
