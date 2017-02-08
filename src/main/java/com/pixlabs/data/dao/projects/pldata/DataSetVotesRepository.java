package com.pixlabs.data.dao.projects.pldata;

import com.pixlabs.data.entities.projects.DataSetVotes;
import com.pixlabs.data.entities.projects.pldata.DataSet;
import com.pixlabs.data.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pix-i on 08/02/2017.
 * ${Copyright}
 */
public interface DataSetVotesRepository extends JpaRepository<DataSetVotes,Long> {

    DataSetVotes findByUser(User user);
    DataSetVotes findByProject(DataSet dataSet);

    @Override
    void delete(DataSetVotes dataSetVotes);

}
