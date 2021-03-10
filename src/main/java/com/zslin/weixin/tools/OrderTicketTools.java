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
//        return VALUE + SEP + orderNo;
        return buildTicketNo(null, VALUE, orderNo);
    }

    /**
     * 生成领券码
     * @param money 具体金额
     * @param orderNo 订单编号
     * @return
     */
    public String buildTicketNo(String sn, String money, String orderNo) {
        if(sn==null || "".equalsIgnoreCase(sn.trim())) {
            return money + SEP + orderNo;
        } else {
            //金额_SN_订单编号
            return money + SEP + sn + SEP + orderNo;
        }
    }
}
