package com.zslin.multi.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.model.Moneybag;
import com.zslin.multi.model.MoneybagDetail;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.HlxTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 解冻金额
 *  - 专门用于解冻会员充值的冻结金额
 */
@Component
public class UnfreezeTimer {

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @Autowired
    private IMoneybagDao moneybagDao;

    //每天2点自动处理
    @Scheduled(cron = "0 0 2 * * ?")
    public void dataByDay() {
        //所有冻结数据
        List<MoneybagDetail> detailList = moneybagDetailDao.listAllFreezeDetail();
        for(MoneybagDetail detail : detailList) {
            //1. 修改详情的冻结标记为不冻结
            detail.setFreezeFlag("0");
            moneybagDetailDao.save(detail);

            //2. 获取对应钱包
            Moneybag bag = moneybagDao.findByPhone(detail.getPhone());
            if(bag.getFreezeMoney()>=detail.getMoney()) {
                //3. 钱包可用金额增加
                bag.setMoney(bag.getMoney()+detail.getMoney());
                //4. 钱包冻结金额减少
                bag.setFreezeMoney(bag.getFreezeMoney()-detail.getMoney());
                moneybagDao.save(bag);
            }
        }
    }
}
