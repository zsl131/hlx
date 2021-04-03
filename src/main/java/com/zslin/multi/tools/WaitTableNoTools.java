package com.zslin.multi.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.model.OrderNo;
import com.zslin.client.service.IOrderNoService;
import com.zslin.multi.dao.IWaitTableNoDao;
import com.zslin.multi.model.WaitTableNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2021/03/15
 * 排队编号工具类
 */
@Component
public class WaitTableNoTools {

    @Autowired
    private IWaitTableNoDao waitTableNoDao;

    public synchronized String getWaitTableNo(String storeSn, String type) {
        String days = NormalTools.curDate("yyyyMMdd");
        Integer curNo = waitTableNoDao.findNo(storeSn, days, type);
        if(curNo==null || curNo<=0) {
            curNo = 0;
            WaitTableNo wt = new WaitTableNo();
            wt.setCurNo(1);
            wt.setDays(days);
            wt.setCateFlag(type);
            waitTableNoDao.save(wt);
        } else {
            waitTableNoDao.updateNo(storeSn, days, type);
        }
        return buildShortNo(type, curNo);
    }

    //微信上的订单号以3开始
    private String buildShortNo(String type, Integer orderNo) {
        String str = ""+(orderNo+1);
        StringBuffer sb = new StringBuffer(type);
        for(int i=0;i<2-str.length();i++) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }
}
