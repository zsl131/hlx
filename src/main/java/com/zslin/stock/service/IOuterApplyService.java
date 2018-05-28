package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.OuterApply;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IOuterApplyService extends BaseRepository<OuterApply, Integer>, JpaSpecificationExecutor<OuterApply> {

    OuterApply findByBatchNo(String batchNo);

    @Query("SELECT MAX(no) FROM OuterApply")
    Integer maxNo();

    @Query("UPDATE OuterApply set status=?2 WHERE batchNo=?1")
    @Modifying
    @Transactional
    void updateStatus(String batchNo, String status);
}
