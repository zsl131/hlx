package com.zslin.wx.dbtools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.WalletDetail;
import com.zslin.web.service.IWalletDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 17:06.
 */
@Component
public class WalletDetailTools {

    @Autowired
    private IWalletDetailService walletDetailService;

    public void addWalletDetail(Integer amount, String openid, Integer accountId, String accountName, String tranType, String reason, String type) {
        WalletDetail wd = new WalletDetail();
        wd.setCreateDate(new Date());
        wd.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        wd.setCreateLong(System.currentTimeMillis());
        wd.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
        wd.setAccountName(accountName);
        wd.setAccountId(accountId);
        wd.setOpenid(openid);
        wd.setAmount(amount);
        wd.setSurplus(amount); //添加时剩余与原始一样
        wd.setFlag(amount>0?"1":"-1");
        wd.setReason(reason);
        wd.setTranType(tranType);
        wd.setType(type);
        walletDetailService.save(wd);
    }

    public void addWalletDetailScore(Integer amount, String openid, Integer accountId, String accountName, String tranType, String reason) {
        addWalletDetail(amount, openid, accountId, accountName, tranType, reason, "2");
    }

    public void addWalletDetailMoney(Integer amount, String openid, Integer accountId, String accountName, String tranType, String reason) {
        addWalletDetail(amount, openid, accountId, accountName, tranType, reason, "1");
    }
}
