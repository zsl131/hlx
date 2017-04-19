package com.zslin.kaoqin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.kaoqin.model.Workday;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 15:51.
 */
public interface IWorkdayService extends BaseRepository<Workday, Integer>, JpaSpecificationExecutor<Workday> {

    Workday findByWorkerId(Integer workerId);

    @Query("DELETE FROM Workday w WHERE w.workerId=?1")
    @Modifying
    @Transactional
    void deleteByWorkerId(Integer workerId);
}
