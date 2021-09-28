package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.Preenter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IPreenterService extends BaseRepository<Preenter, Integer>, JpaSpecificationExecutor<Preenter> {

    Preenter findByBatchNo(String batchNo);

    @Query("SELECT MAX(no) FROM Preenter WHERE storeSn=?1")
    Integer maxNo(String storeSn);

    @Query("UPDATE Preenter set status=?2 WHERE batchNo=?1")
    @Modifying
    @Transactional
    void updateStatus(String batchNo, String status);

    @Query("SELECT status FROM Preenter WHERE batchNo=?1")
    String findStatusByBatchNo(String batchNo);
}
