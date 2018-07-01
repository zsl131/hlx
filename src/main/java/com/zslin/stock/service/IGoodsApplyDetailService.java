package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsApplyDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsApplyDetailService extends BaseRepository<GoodsApplyDetail, Integer>, JpaSpecificationExecutor<GoodsApplyDetail> {

    GoodsApplyDetail findByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Transactional
    @Modifying
    void deleteByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Query("FROM GoodsApplyDetail WHERE batchNo=?1")
    List<GoodsApplyDetail> listByBatchNo(String batchNo);

    @Query("UPDATE GoodsApplyDetail SET amountTrue=?3 WHERE batchNo=?1 AND goodsId=?2")
    @Modifying
    @Transactional
    void updateAmountTrue(String batchNo, Integer goodsId, Integer amountTrue);

    @Query("UPDATE GoodsApplyDetail SET allowAmount=amount WHERE batchNo=?1 AND hasCheck='0'")
    @Modifying
    @Transactional
    void updateAmountAllow(String batchNo);
}
