package com.pixlabs.data.entities.projects.pldata;

import com.pixlabs.data.entities.projects.Project;
import com.pixlabs.data.entities.user.User;

import javax.persistence.*;
import java.util.*;

/**
 * Created by pix-i on 07/02/2017.
 * ${Copyright}
 */
@Entity
public class DataSet{

    @Id
    @GeneratedValue
    private long id;

    private String name;



    @OneToMany
    @JoinTable(
            name="dataSet_unit",
            joinColumns = {@JoinColumn(name = "dataSet_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dataUnit_id",referencedColumnName = "id")}
    )
    private List<DataUnit> dataList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "projects_dataSets",
            joinColumns = {@JoinColumn(name = "project_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="dataSet_id",referencedColumnName = "id")}

    )
    private List<Project> projectList = new ArrayList<>();

    private boolean isPublic;

    @ManyToOne
    private User dataSetOwner;

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public User getDataSetOwner() {
        return dataSetOwner;
    }

    public void setDataSetOwner(User dataSetOwner) {
        this.dataSetOwner = dataSetOwner;
    }

    private boolean open = true;

    private DataSetType type;


    private Date lastUpdate;

    public DataSet() {
        this.lastUpdate = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ArrayList<DataUnit> getDataList() {
        return (ArrayList<DataUnit>) dataList;
    }

    public void setDataList(ArrayList<DataUnit> dataList) {
        this.dataList = dataList;
    }

    public DataSetType getType() {
        return type;
    }

    public void setType(DataSetType type) {
        this.type = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDataUnit(DataUnit unit) {
        this.dataList.add(unit);
    }



    public void addProject(Project project){ this.projectList.add(project);}
    public void removeProject(Project project) { this.projectList.remove(project);}

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }



}
