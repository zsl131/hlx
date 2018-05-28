package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.OuterApplyDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IOuterApplyDetailService extends BaseRepository<OuterApplyDetail, Integer>, JpaSpecificationExecutor<OuterApplyDetail> {

    OuterApplyDetail findByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Transactional
    @Modifying
    void deleteByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Query("FROM OuterApplyDetail WHERE batchNo=?1")
    List<OuterApplyDetail> listByBatchNo(String batchNo);

    @Query("UPDATE OuterApplyDetail SET amountTrue=?3 WHERE batchNo=?1 AND goodsId=?2")
    @Modifying
    @Transactional
    void updateAmountTrue(String batchNo, Integer goodsId, Integer amountTrue);
}
