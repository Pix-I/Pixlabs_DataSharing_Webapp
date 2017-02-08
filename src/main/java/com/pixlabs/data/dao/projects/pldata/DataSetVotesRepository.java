package com.pixlabs.data.dao.projects.pldata;

import com.pixlabs.data.entities.projects.pldata.DataSetVotes;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */
public interface DataSetVotesRepository extends JpaRepository<DataSetVotes,Long> {

    @Override
    void delete(DataSetVotes dataSetVotes);

}
