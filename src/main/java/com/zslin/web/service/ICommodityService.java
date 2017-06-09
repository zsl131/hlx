package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Commodity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/1 9:45.
 */
public interface ICommodityService extends BaseRepository<Commodity, Integer>, JpaSpecificationExecutor<Commodity> {

    Commodity findByNo(String no);

    @Query("DELETE Commodity WHERE no=?1")
    @Transactional
    @Modifying
    void deleteByNo(String no);

    /** 获取券类商品 */
    @Query("FROM Commodity c WHERE c.type='1' OR c.type='2' ORDER BY c.type ASC")
    List<Commodity> listByTicket();

    @Query("FROM Commodity c WHERE c.type=?1")
    List<Commodity> listByType(String type);

    @Query("SELECT c.price FROM Commodity c WHERE c.no=?1")
    Float queryPrice(String no);
}
