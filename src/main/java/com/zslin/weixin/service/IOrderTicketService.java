package com.zslin.weixin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.weixin.model.OrderTicket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IOrderTicketService extends BaseRepository<OrderTicket, Integer>, JpaSpecificationExecutor<OrderTicket> {

    OrderTicket findByOrderNo(String orderNo);

    @Query("UPDATE OrderTicket t SET t.status=?1 WHERE t.orderNo=?2 ")
    @Modifying
    @Transactional
    void updateStatus(String status, String orderNo);
}
