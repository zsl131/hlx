package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Clockin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/18 11:37.
 */
public interface IClockinService extends BaseRepository<Clockin, Integer>, JpaSpecificationExecutor<Clockin> {

    @Query("FROM Clockin c WHERE c.year=?1 AND c.month=?2 AND c.workerId=?3 ORDER BY c.day ASC")
    List<Clockin> findByWorker(Integer year, Integer month, Integer workerId);

    @Query("FROM Clockin c WHERE c.year=?1 AND c.month=?2 GROUP BY c.workerId ")
    List<Clockin> findByGroup(Integer year, Integer month);
}
