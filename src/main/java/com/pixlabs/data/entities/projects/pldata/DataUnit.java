package com.pixlabs.data.entities.projects.pldata;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by pix-i on 07/02/2017.
 * ${Copyright}
 */

@Entity
public class DataUnit {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @ManyToOne
    private DataSet owner;

    @NotNull
    private long value;

    public DataUnit(DataSet owner, String key, Long value) {
        this.owner = owner;
        this.name = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o){
        if(o==null)
            return false;
        if(o instanceof DataUnit){
            DataUnit unit = (DataUnit)o;
            return unit.getName().equals(name) && unit.getValue()==value;

        }
        return false;
    }

    public DataUnit(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSet getOwner() {
        return owner;
    }

    public void setOwner(DataSet owner) {
        this.owner = owner;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
