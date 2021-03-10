package com.zslin.web.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Rules;
import com.zslin.web.model.WalletDetail;
import com.zslin.web.service.IRulesService;
import com.zslin.web.service.IWalletDetailService;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.dbtools.ScoreAdditionalDto;
import com.zslin.wx.dto.TemplateParamDto;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/27 23:56.
 */
@Component
public class ScoreTimerTools {

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private EventTools eventTools;

    //每天23点27分50秒时执行
    @Scheduled(cron = "50 27 23 * * ?")
    public void processScore() {
        Rules rules = rulesService.findByStoreSn();
        Integer overdueDays = rules.getScoreOverdueDays(); //积分过期天数
        if(overdueDays!=null && overdueDays>0) { //有积分过期天数表示积分会过期
            List<WalletDetail> datas = walletDetailService.findOverdue(buildOverdueLong(overdueDays));
            String openid = "";
            Integer score = 0;
            StringBuffer sb = new StringBuffer("0,"); //所有记录ID，需要全部更新为已过期
            for(WalletDetail wd : datas) {
                sb.append(wd.getId()).append(",");
                if(!openid.equals(wd.getOpenid())) {
                    if(openid!=null && !"".equals(openid)) {
                        //TODO 通知减分
                    }
                    openid = wd.getOpenid();
                    score = wd.getSurplus(); //获取剩余
                } else {
                    score += wd.getSurplus(); //获取累计剩余
                }
            }
            sb.append("0");
        }
    }

    private void notice(String openid, Integer score, Integer scoreMoney) {
        if(score>0) {
            double money = 0;
            if(scoreMoney!=null && scoreMoney>0) {
                money = (score*1.0d)/scoreMoney;
                money = NormalTools.buildPoint(money);
            }
            walletService.plusScore(0 - score, openid);
            eventTools.sendTemplateMessage("积分过期提醒", openid, "尊敬的用户，您有积分已过期", "", "/wx/account/score",
                    new TemplateParamDto("积分数量", score+""),
                    new TemplateParamDto("过期时间", NormalTools.curDate("yyyy年MM月dd日")),
                    new TemplateParamDto("损失金额", money+" 元"));
        }
    }

    //当前时间减去过期天数
    private Long buildOverdueLong(Integer overdueDays) {
        Long curLong = System.currentTimeMillis();
        return curLong - (overdueDays*24*3600*1000);
    }
}
