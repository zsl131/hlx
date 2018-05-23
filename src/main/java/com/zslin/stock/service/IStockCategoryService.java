package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.model.StockCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IStockCategoryService extends BaseRepository<StockCategory, Integer>, JpaSpecificationExecutor<StockCategory> {

    @Query("FROM StockCategory sc WHERE sc.locationType=?1")
    List<StockCategory> listByLocationType(String locationType);
}
