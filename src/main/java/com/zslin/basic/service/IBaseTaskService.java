package com.zslin.basic.service;

import com.zslin.basic.model.BaseTask;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBaseTaskService extends BaseRepository<BaseTask, Integer>, JpaSpecificationExecutor<BaseTask> {

    BaseTask findByTaskUuid(String taskName);

    @Query("UPDATE BaseTask b SET b.status=?1 WHERE b.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    @Query("UPDATE BaseTask b SET b.status=?1 WHERE b.taskUuid=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, String taskUuid);

    @Query("UPDATE BaseTask b SET b.sucCount=b.sucCount+?1, b.errCount=b.errCount+?2 WHERE b.id=?3")
    @Modifying
    @Transactional
    void updateCount(Integer sucCount, Integer errCount, Integer id);

    @Query("FROM BaseTask b WHERE b.status=?1")
    List<BaseTask> listByStatus(String status);
}
