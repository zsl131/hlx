package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.StockCheckDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IStockCheckDetailService extends BaseRepository<StockCheckDetail, Integer>, JpaSpecificationExecutor<StockCheckDetail> {

    StockCheckDetail findByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Transactional
    @Modifying
    void deleteByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Query("FROM StockCheckDetail WHERE batchNo=?1")
    List<StockCheckDetail> listByBatchNo(String batchNo);

    @Query("UPDATE StockCheckDetail SET amountNow=?3 WHERE batchNo=?1 AND goodsId=?2")
    @Modifying
    @Transactional
    void updateAmountNow(String batchNo, Integer goodsId, Integer amountTrue);
}
