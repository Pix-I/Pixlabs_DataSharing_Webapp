package com.pixlabs.data.entities.util;

import com.pixlabs.data.entities.projects.ProjectTag;

import java.util.Comparator;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */
public class CompareTagByName implements Comparator<ProjectTag> {
    @Override
    public int compare(ProjectTag o1, ProjectTag o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
