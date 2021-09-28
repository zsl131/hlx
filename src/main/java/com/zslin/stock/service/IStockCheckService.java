package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.StockCheck;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IStockCheckService extends BaseRepository<StockCheck, Integer>, JpaSpecificationExecutor<StockCheck> {

    StockCheck findByBatchNo(String batchNo);

    @Query("SELECT MAX(no) FROM StockCheck WHERE storeSn=?1")
    Integer maxNo(String storeSn);

    @Query("UPDATE StockCheck set status=?2 WHERE batchNo=?1")
    @Modifying
    @Transactional
    void updateStatus(String batchNo, String status);

    @Query("SELECT status FROM StockCheck WHERE batchNo=?1")
    String findStatusByBatchNo(String batchNo);
}
