package com.pixlabs.data.dao.projects.pldata;

import com.pixlabs.data.entities.projects.pldata.DataUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 07/02/2017.
 * ${Copyright}
 */
public interface DataUnitRepository extends JpaRepository<DataUnit,Long> {

    DataUnit findByName(String name);
    DataUnit findById(long id);

    @Override
    void delete(DataUnit dataUnit);
}