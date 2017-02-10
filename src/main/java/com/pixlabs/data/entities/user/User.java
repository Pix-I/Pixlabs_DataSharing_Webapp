package com.pixlabs.data.entities.user;

import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.projects.ProjectVote;
import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.projects.pldata.DataSetVote;

import javax.persistence.*;
import java.util.*;

/**
 * Created by pix-i on 13/01/2017.
 * ${Copyright}
 */

@Entity
@Table(name = "user")
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

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "profilePicture")
    @Lob
    private byte[] profilePicture;

    @OneToMany
    @JoinTable
            (
                    name="user_projects",
                    joinColumns={ @JoinColumn(name="user_id", referencedColumnName="id") },
                    inverseJoinColumns={ @JoinColumn(name="project_id", referencedColumnName="id", unique=true) }
            )
    private List<Project> projects;


    @OneToMany
    @JoinTable(
            name = "user_projectVotes",
            joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="projectVote_id",referencedColumnName = "id")}
    )
    private List<ProjectVote> projectVotes = new LinkedList<>();

    @OneToMany
    @JoinTable(
            name = "user_dataSetVotes",
            joinColumns = {@JoinColumn(name="user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="dataSetVote_id",referencedColumnName = "id")}
    )
    private List<DataSetVote> dataSetVotes = new LinkedList<>();


    public List<DataSet> getDataSets() {
        return dataSets;
    }

    @OneToMany

    private List<DataSet> dataSets = new ArrayList<>();

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


    public void addProject(Project project){
        if(projects==null){
            projects = new ArrayList<>();
        }
        this.projects.add(project);
        if(project.getOwner()!=this){
            project.setOwner(this);
        }
    }

    public User() {
        super();
        this.secret = UUID.randomUUID().toString();
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
        this.email = email.toLowerCase();
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

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void addDataset(DataSet dataSet) {
        this.dataSets.add(dataSet);
    }

    public List<ProjectVote> getProjectVotes() {
        return projectVotes;
    }

    public void setProjectVotes(List<ProjectVote> projectVotes) {
        this.projectVotes = projectVotes;
    }

    public List<DataSetVote> getDataSetVotes() {
        return dataSetVotes;
    }

    public void setDataSetVotes(List<DataSetVote> dataSetVotes) {
        this.dataSetVotes = dataSetVotes;
    }

    public User addDataSetVote(DataSetVote vote){
        if(!this.dataSetVotes.contains(vote)){
            dataSetVotes.add(vote);
    }
    return this;}
    public User addProjectVote(ProjectVote vote){
        projectVotes.removeIf(i->i.getProject().equals(vote.getProject()));
        projectVotes.add(vote);
        return this;
    }

}
