package com.pixlabs.data.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by pix-i on 13/01/2017.
 * ${Copyright}
 */

@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(length = 60,name = "password")
    private String password;

    @Column(length = 253,name = "secret")
    private String secret;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany
    @JoinTable(name = "users_projects",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id",referencedColumnName = "id"))
    private Collection<Project> projects;

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;



    public User() {
        super();
        this.secret = "2344324123413214523125ERDZ?";
        this.enabled = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((email == null) ? 0 : email.hashCode());
        return result;
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
        final User user = (User) obj;
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", secret='" + secret + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public User setProjects(Collection<Project> projects) {
        this.projects = projects;
        return this;
    }


}
