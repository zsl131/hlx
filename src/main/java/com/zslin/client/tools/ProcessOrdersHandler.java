package com.zslin.client.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.client.model.Orders;
import com.zslin.client.service.IOrdersService;
import com.zslin.web.service.IAccountService;
import com.zslin.wx.dto.EventRemarkDto;
import com.zslin.wx.tools.EventTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/16 23:15.
 */
@Component
public class ProcessOrdersHandler {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private EventTools eventTools;

    //处理订单
    public void updateOrders(JSONObject jsonObj) {
        Orders orders = JSON.toJavaObject(JSON.parseObject(jsonObj.toString()), Orders.class);
        String no = orders.getNo();
        Orders o = ordersService.findByNo(no);
        if(o==null){
            ordersService.save(orders);
        } else {
            MyBeanUtils.copyProperties(orders, o);
            ordersService.save(o);
        }
        noticeAdmin(orders);
    }

    private void noticeAdmin(Orders o) {
        //如果是友情价订单并且状态为刚下订单，则通过管理员
        if("0".equalsIgnoreCase(o.getStatus()) && "4".equalsIgnoreCase(o.getType())) {
            //当有友情价是discountReason必须存老板手机号码
            String openid = accountService.findOpenidByPhone(o.getDiscountReason());
            if(openid!=null) {
                eventTools.eventRemind(openid,"友情价折扣提醒", "有顾客需要友情价", NormalTools.curDate("yyyy-MM-dd HH:mm"),
                        "/wx/orders/confirmFriend?no="+o.getNo(),
                        new EventRemarkDto("订单编号", o.getNo()),
                        new EventRemarkDto("全票人数", o.getPeopleCount()+" 人"),
                        new EventRemarkDto("半票人数", o.getHalfCount()+ " 人"),
                        new EventRemarkDto("免票人数", o.getChildCount()+ " 人"),
                        new EventRemarkDto("", "点击查看可确认！"));
            }
        }
    }
}
