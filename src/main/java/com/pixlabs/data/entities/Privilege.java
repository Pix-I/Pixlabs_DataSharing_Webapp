package com.pixlabs.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * Created by pix-i on 17/01/2017.
 * ${Copyright}
 */


@Entity
public class Privilege {

    @Id
    @GeneratedValue
    private long id;

    private String username;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(){}

    public Privilege(final String username){
        super();
        this.username = username;
    }

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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }



    @Override
    public String toString() {
        return "Privilege{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
