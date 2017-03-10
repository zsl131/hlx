package com.zslin.web.tools;

import com.zslin.cache.CacheTools;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/8 17:21.
 * 收银员工具类，主要用于设置当前的收银员的openid
 */
@Component
public class CashierTools {

    @Autowired
    private CacheTools cacheTools;

    @Autowired
    private IWorkerService workerService;

    private static final String CASHIER_CACHE_OPENID = "current-cachier";

    /**
     * 设置当前收银人员Openid
     * @param openid 收银人员Openid
     */
    public void setCurrentCashier(String openid) {
        cacheTools.putKey(CASHIER_CACHE_OPENID, openid, 20*60*60); //设置为20小时
    }

    /**
     * 获取当前的收银人员的Openid，如果在缓存中不存在就取员工的收银人员
     * @return
     */
    public List<String> getCurrentCashier() {
        String openid = (String) cacheTools.getKey(CASHIER_CACHE_OPENID);
        List<String> res = null;
        if(openid==null || "".equals(openid)) { //如果缓存中不存在，则从员工中取
            res = workerService.findCashierOpenid();
        } else {
            res = new ArrayList<>();
            res.add(openid);
        }
        return res;
    }
}
