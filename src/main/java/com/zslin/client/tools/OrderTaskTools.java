package com.zslin.client.tools;

import com.zslin.web.model.BuffetOrder;
import com.zslin.web.service.IBuffetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单定时处理任务工具
 * Created by zsl on 2018/12/26.
 */
@Component
public class OrderTaskTools {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private ShopTools shopTools;

    @Scheduled(cron = "0 30 22 * * ?")
    public void reTakeOff() {
        List<BuffetOrder> list = buffetOrderService.findNoTakeOff();
        System.out.println("-----执行 reTakeOff ("+list.size()+")------");
        for(BuffetOrder order : list) {
            shopTools.onShoppingNoStatus(order);
        }
    }
}
