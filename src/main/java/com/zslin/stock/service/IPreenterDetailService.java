package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.PreenterDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IPreenterDetailService extends BaseRepository<PreenterDetail, Integer>, JpaSpecificationExecutor<PreenterDetail> {

    PreenterDetail findByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Transactional
    @Modifying
    void deleteByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Query("FROM PreenterDetail WHERE batchNo=?1")
    List<PreenterDetail> listByBatchNo(String batchNo);

    @Query("UPDATE PreenterDetail SET amountTrue=?3 WHERE batchNo=?1 AND goodsId=?2")
    @Modifying
    @Transactional
    void updateAmountTrue(String batchNo, Integer goodsId, Integer amountTrue);
}
