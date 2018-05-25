package com.zslin.stock.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.stock.dto.GoodsDto;
import com.zslin.stock.model.StockGoods;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/5/21.
 */
public interface IStockGoodsService extends BaseRepository<StockGoods, Integer>, JpaSpecificationExecutor<StockGoods> {

    @Query("SELECT MAX(orderNo) FROM StockGoods")
    Integer maxOrderNo();

    @Query("UPDATE StockGoods s SET s.status=?1 WHERE s.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    @Query("UPDATE StockGoods s SET s.hasWarn=?1 WHERE s.id=?2")
    @Modifying
    @Transactional
    void updateHasWarn(String hasWarn, Integer id);

    @Query("SELECT new com.zslin.stock.dto.GoodsDto(id, amount) FROM StockGoods WHERE id in (?1)")
    List<GoodsDto> listByIds(Integer... ids);
}
