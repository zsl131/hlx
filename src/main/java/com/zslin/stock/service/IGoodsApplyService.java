package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsApply;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsApplyService extends BaseRepository<GoodsApply, Integer>, JpaSpecificationExecutor<GoodsApply> {

    GoodsApply findByBatchNo(String batchNo);

    @Query("SELECT MAX(no) FROM GoodsApply")
    Integer maxNo();

    @Query("UPDATE GoodsApply set status=?2 WHERE batchNo=?1")
    @Modifying
    @Transactional
    void updateStatus(String batchNo, String status);
}
