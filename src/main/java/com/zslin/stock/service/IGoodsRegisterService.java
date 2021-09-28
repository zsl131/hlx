package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.GoodsRegister;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IGoodsRegisterService extends BaseRepository<GoodsRegister, Integer>, JpaSpecificationExecutor<GoodsRegister> {

    GoodsRegister findByBatchNo(String batchNo);

    @Query("SELECT MAX(no) FROM GoodsRegister WHERE storeSn=?1")
    Integer maxNo(String storeSn);
}
