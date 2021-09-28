package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.StockUserStore;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IStockUserStoreService extends BaseRepository<StockUserStore, Integer>, JpaSpecificationExecutor<StockUserStore> {

    StockUserStore findByUserIdAndStoreId(Integer userId, Integer storeId);
}
