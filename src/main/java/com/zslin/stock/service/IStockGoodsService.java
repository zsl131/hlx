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

    @Query("SELECT MAX(orderNo) FROM StockGoods WHERE storeSn=?1")
    Integer maxOrderNo(String storeSn);

    @Query("UPDATE StockGoods s SET s.status=?1 WHERE s.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    @Query("UPDATE StockGoods s SET s.hasWarn=?1 WHERE s.id=?2")
    @Modifying
    @Transactional
    void updateHasWarn(String hasWarn, Integer id);

    /** 在库存发生变化时，修改通知属性为0 */
    @Query("UPDATE StockGoods s SET s.amount=s.amount+?2, s.hasWarn='0' WHERE s.id=?1")
    @Modifying
    @Transactional
    void plusAmount(Integer id, Integer amount);

    @Query("SELECT new com.zslin.stock.dto.GoodsDto(id, amount) FROM StockGoods WHERE id in (?1)")
    List<GoodsDto> listByIds(Integer... ids);

    @Query("FROM StockGoods WHERE amount>0 AND storeSn=?1")
    List<StockGoods> findByCanOuter(String storeSn);

    /** 用于盘点 */
    @Query("UPDATE StockGoods s SET s.amount=?2 WHERE s.id=?1")
    @Modifying
    @Transactional
    void updateAmount(Integer id, Integer amount);

    /** 获取需要预警的物品 */
    @Query("FROM StockGoods WHERE hasWarn='0' AND amount<=warnAmount AND storeSn=?1")
    List<StockGoods> findByWarn(String storeSn);

    /** 获取需要预警的物品 */
    @Query("FROM StockGoods WHERE amount<=warnAmount AND storeSn=?1")
    List<StockGoods> findAllWarn(String storeSn);

    /** 获取所有物品，用于显示给客人看的 */
    @Query("FROM StockGoods WHERE status='1' AND storeSn=?1")
    List<StockGoods> findAllShow(String storeSn);
}
