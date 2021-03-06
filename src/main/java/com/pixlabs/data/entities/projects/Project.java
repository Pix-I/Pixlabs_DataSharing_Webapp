package com.pixlabs.data.entities.projects;

import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @ManyToMany(mappedBy = "projectList")
    private List<DataSet> dataSets = new LinkedList<>();

    private String description;

    @NotNull
    private String title;

    public SortedSet<ProjectTag> getTagList() {
        return tagList;
    }

    public void setTagList(SortedSet<ProjectTag> tagList) {
        this.tagList = tagList;
    }

    @ManyToMany
    @OrderBy("name ASC")
    @JoinTable(name = "project_tags",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "projectTag_id", referencedColumnName = "id"))
    private SortedSet<ProjectTag> tagList = new TreeSet<>();

    private Date creationDate;
    private Date modificationDate;

    private int totalUpVotes = 0;

    @OneToMany
    @JoinTable(
            name = "project_projectVotes",
            joinColumns = @JoinColumn(name = "project_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "projectVote_id",referencedColumnName = "id")
    )
    private List<ProjectVote> votes = new LinkedList<>();

    public List<ProjectVote> getVotes() {
        return votes;
    }

    public void setVotes(LinkedList<ProjectVote> votes) {
        this.votes = votes;
    }

    private boolean restricted = true;

    public Project() {
        this.restricted = true;
        this.creationDate = new Date();
        this.modificationDate = this.creationDate;
    }


    public void setUser(User user) {
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
        if (project.getId()!=id) {
            return false;
        }
        return true;
    }

    public long getId() {
        return id;
    }
    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
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

    public List<DataSet> getDataSets() {
        return dataSets;
    }

    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
    }

    public void addDataSet(DataSet dataSet) {
        this.dataSets.add(dataSet);
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public int getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(int totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", owner=" + owner +
                ", dataSets=" + dataSets +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", tagList=" + tagList +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", totalUpVotes=" + totalUpVotes +
                ", votes=" + votes +
                ", restricted=" + restricted +
                '}';
    }

    public void addVote(ProjectVote projectVote) {
        totalUpVotes += projectVote.getVote();
        this.votes.add(projectVote);
    }

    public ProjectVote updateVote(User activeUser, int value) {
        totalUpVotes +=value;
        for(ProjectVote vote:votes){
            if(vote.getUser().equals(activeUser)) {
                totalUpVotes -=vote.getVote();
                vote.setVote(value);
                return vote;
            }
        }
        ProjectVote vote = new ProjectVote(activeUser,this,value);
        votes.add(vote);
        return vote;
    }
}
