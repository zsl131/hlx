package com.zslin.client.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.model.OrderNo;
import com.zslin.client.service.IOrderNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 15:55.
 * 订单编号工具类
 */
@Component
public class OrderNoTools {

    @Autowired
    private IOrderNoService orderNoService;

    public synchronized  String getWeixinOrderNo() {
        return getOrderNo("3");
    }

    public synchronized String getOrderNo(String type) {
        String days = NormalTools.curDate("yyyyMMdd");
        Integer curNo = orderNoService.findOrderNo(days, type);
        if(curNo==null || curNo<=0) {
            curNo = 0;
            OrderNo on = new OrderNo();
            on.setCurNo(1);
            on.setDays(days);
            on.setType(type);
            orderNoService.save(on);
        } else {
            orderNoService.updateOrderNo(days, type);
        }
        return days+buildShortNo(type, curNo);
    }

    //微信上的订单号以3开始
    private String buildShortNo(String type, Integer orderNo) {
        String str = ""+(orderNo+1);
        StringBuffer sb = new StringBuffer(type);
        for(int i=0;i<3-str.length();i++) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }
}
