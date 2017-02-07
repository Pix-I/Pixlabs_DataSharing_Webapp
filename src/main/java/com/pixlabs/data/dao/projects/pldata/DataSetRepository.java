package com.pixlabs.data.dao.projects.pldata;

import com.pixlabs.data.entities.projects.pldata.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 07/02/2017.
 * ${Copyright}
 */
public interface DataSetRepository extends JpaRepository<DataSet,Long> {
    DataSet findByName(String name);

    @Override
    void delete(DataSet dataSet);
}
