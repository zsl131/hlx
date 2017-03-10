package com.zslin.wx.dbtools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Account;
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

    /**
     * 处理账户余额
     * @param openid 用户openid
     * @param money 操作金额，单位分
     * @param type 操作原因
     * @param dtoList 其他通知信息
     */
    public void processScore(String openid, Integer money, String type, ScoreAdditionalDto... dtoList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(openid==null || "".equals(openid)) {return;}
                Account a = accountService.findByOpenid(openid);
                walletDetailTools.addWalletDetailMoney(money, a.getOpenid(), a.getId(), a.getNickname(), type, type);
                walletService.plusMoney(money, a.getOpenid());

                StringBuffer sb = new StringBuffer();
                sb.append("账户增加：").append(money*1.0/100).append(" 元\\n")
                        .append("账户剩余：").append(walletService.queryMoney(a.getOpenid())*1.0/100).append(" 元\\n")
                        .append("变化原因：").append(type);

                for(ScoreAdditionalDto dto : dtoList) {
                    sb.append("\\n").append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue());
                }
//                        .append("点赞食品：").append(foodName);
                eventTools.eventRemind(a.getOpenid(), "账户余额变化提醒", "账户余额发生变化啦~~", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/money");
            }
        }).start();
    }


}
