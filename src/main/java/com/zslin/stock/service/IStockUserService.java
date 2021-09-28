package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.StockUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IStockUserService extends BaseRepository<StockUser, Integer>, JpaSpecificationExecutor<StockUser> {

    StockUser findByPhone(String phone);

    @Query("SELECT sus.storeId FROM StockUserStore sus WHERE sus.userId=?1")
    List<Integer> listStoreIds(Integer userId);

    @Query("SELECT sus.storeSn FROM StockUserStore sus WHERE sus.userId=?1")
    List<String> listStoreSns(Integer userId);

    @Query("UPDATE StockUser su SET su.storeSns=?1 WHERE su.id=?2")
    @Modifying
    @Transactional
    void updateStoreSns(String sns, Integer userId);
}
