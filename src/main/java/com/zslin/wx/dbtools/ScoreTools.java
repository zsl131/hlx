package com.zslin.wx.dbtools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.Rules;
import com.zslin.web.model.ScoreRule;
import com.zslin.web.model.Wallet;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IScoreRuleService;
import com.zslin.web.service.IWalletDetailService;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/2 8:59.
 */
@Component
public class ScoreTools {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IScoreRuleService scoreRuleService;

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private WalletDetailTools walletDetailTools;

    @Autowired
    private EventTools eventTools;

    public void processScore(String openid, String scoreRuleType, ScoreAdditionalDto... dtoList) {
        processScore(true, openid, scoreRuleType, dtoList);
    }

    public void processScore(boolean needNoticeClient, String openid, String scoreRuleType, ScoreAdditionalDto... dtoList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(openid==null || "".equals(openid)) {return;}
                Account a = accountService.findByOpenid(openid);
                ScoreRule sr = scoreRuleService.findByCode(scoreRuleType); //
                if(sr!=null) {
                    Integer count = 0;
                    if(ScoreRule.INIT.equals(scoreRuleType)) {
                        count = walletDetailService.queryCount(scoreRuleType, a.getOpenid());
                    } else {
                        count = walletDetailService.queryCount(scoreRuleType, NormalTools.curDate("yyyy-MM-dd"), a.getOpenid());
                    }
                    if(count<sr.getAmount()) {
                        Integer score = sr.getScore();
                        walletDetailTools.addWalletDetailScore(score, a.getPhone(), a.getOpenid(), a.getId(), a.getNickname(), scoreRuleType, sr.getName());
                        walletService.plusScore(score, a.getOpenid());

                        if(needNoticeClient) { //是否需要通知客户端修改积分
                            Wallet w = walletService.findByOpenid(openid);
                            if (w != null && w.getPhone() != null && !"".equalsIgnoreCase(w.getPhone())) {
                                send2Client(w);
                            }
                        }

                        StringBuffer sb = new StringBuffer();
                        sb.append("积分增加：").append(score).append(" 分\\n")
                                .append("当前剩余：").append(walletService.queryScore(a.getOpenid())).append(" 分\\n")
                                .append("变化原因：").append(sr.getName());

                        for(ScoreAdditionalDto dto : dtoList) {
//                            sb.append("\\n").append(dto.getName()).append("：").append(dto.getValue());
                            sb.append("\\n").append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue());
                        }
//                        .append("点赞食品：").append(foodName);
                        eventTools.eventRemind(a.getOpenid(), "积分变化提醒", "积分发生变化啦~~", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/score");
                    }
                }
            }
        }).start();
    }

    @Autowired
    private ClientFileTools clientFileTools;

    private void send2Client(Wallet w) {
        String json = ClientJsonTools.buildDataJson(ClientJsonTools.buildWallet(w));
        clientFileTools.setChangeContext(json, true);
    }

    public void processScoreByAmount(boolean needNoticeClient, String openid, Integer amount, String reason, ScoreAdditionalDto... dtoList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(openid==null || "".equals(openid)) {return;}
                Account a = accountService.findByOpenid(openid);
                walletDetailTools.addWalletDetailScore(amount, a.getPhone(), a.getOpenid(), a.getId(), a.getNickname(), reason, reason);
                walletService.plusScore(amount, a.getOpenid());

                if(needNoticeClient) { //是否需要通知客户端修改积分
                    Wallet w = walletService.findByOpenid(openid);
                    if (w != null && w.getPhone() != null && !"".equalsIgnoreCase(w.getPhone())) {
                        send2Client(w);
                    }
                }

                StringBuffer sb = new StringBuffer();
                sb.append("积分增加：").append(amount).append(" 分\\n")
                        .append("当前剩余：").append(walletService.queryScore(a.getOpenid())).append(" 分\\n")
                        .append("变化原因：").append(reason);

                for(ScoreAdditionalDto dto : dtoList) {
//                            sb.append("\\n").append(dto.getName()).append("：").append(dto.getValue());
                    sb.append("\\n").append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue());
                }
//                        .append("点赞食品：").append(foodName);
                eventTools.eventRemind(a.getOpenid(), "积分变化提醒", "积分发生变化啦~~", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/score");
            }
        }).start();
    }

    public void processScoreByAmount(String openid, Integer amount, String reason, ScoreAdditionalDto... dtoList) {
        processScoreByAmount(true, openid, amount, reason, dtoList);
    }
}
