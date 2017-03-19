package com.zslin.wx.dbtools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.service.IMemberService;
import com.zslin.web.model.Account;
import com.zslin.web.model.Wallet;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IWalletDetailService;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/2 8:59.
 */
@Component
public class MoneyTools {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private WalletDetailTools walletDetailTools;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private IMemberService memberService;

    /**
     * 处理账户余额
     * @param phone 用户手机号码
     * @param money 操作金额，单位分
     * @param type 操作原因
     */
    public void processScoreByPhone(String phone, Integer money, String type) {
        if(phone==null || "".equals(phone)) {return;}
        Wallet wallet = walletService.findByPhone(phone);
        String openid = wallet.getOpenid();
        walletDetailTools.addWalletDetailMoney(money/100, openid==null?wallet.getPhone():openid, wallet.getAccountId(), wallet.getAccountName(), type, type);
        walletService.plusMoneyByPhone(money, phone);
        memberService.plusMoneyByPhone(money, phone);

        //如果openid不为空，则通知
        if(openid!=null && !"".equalsIgnoreCase(openid)) {
            StringBuffer sb = new StringBuffer();
            sb.append("账户增加：").append(money * 1.0 / 100).append(" 元\\n")
                    .append("账户剩余：").append(walletService.queryMoney(openid) * 1.0 / 100).append(" 元\\n")
                    .append("变化原因：").append(type);

            eventTools.eventRemind(openid, "账户余额变化提醒", "账户余额发生变化啦~~", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/money");
        }
    }


}
