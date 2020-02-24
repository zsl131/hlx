package com.zslin.client.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.client.model.OrderNo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 15:52.
 */
public interface IOrderNoService extends BaseRepository<OrderNo, Integer> {

    @Query("FROM OrderNo o WHERE o.days=?1 AND o.type=?2")
    OrderNo findByDays(String days, String type);

    @Query("SELECT o.curNo FROM OrderNo o WHERE o.days=?1 AND o.type=?2")
    Integer findOrderNo(String days, String type);

    @Query("UPDATE OrderNo o SET o.curNo=o.curNo+1 WHERE o.days=?1 AND o.type=?2")
    @Modifying
    @Transactional
    void updateOrderNo(String days, String type);
}
