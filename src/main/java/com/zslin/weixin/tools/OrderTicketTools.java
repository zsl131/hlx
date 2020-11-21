package com.zslin.weixin.tools;

import com.zslin.weixin.service.IOrderTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 领券工具类
 */
@Component
public class OrderTicketTools {

    @Autowired
    private IOrderTicketService orderTicketService;

    public static final String VALUE = "100"; //具体金额

    public static final String SEP = "_"; //分隔符

    /**
     * 生成领券码
     * 如： ticket_100_2020102610001
     * @param orderNo 订单编号
     * @return
     */
    public String buildTicketNo(String orderNo) {
        return VALUE + SEP + orderNo;
    }
}
