package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsRegisterDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsRegisterDetailService extends BaseRepository<GoodsRegisterDetail, Integer>, JpaSpecificationExecutor<GoodsRegisterDetail> {

    GoodsRegisterDetail findByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Transactional
    @Modifying
    void deleteByBatchNoAndGoodsId(String batchNo, Integer goodsId);

    @Query("FROM GoodsRegisterDetail WHERE batchNo=?1")
    List<GoodsRegisterDetail> listByBatchNo(String batchNo);
}
