package com.zslin.wx.dbtools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.Own;
import com.zslin.web.model.Prize;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IOwnService;
import com.zslin.wx.dto.TemplateParamDto;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/30 16:57.
 * 处理礼品的工具类
 */
@Component
public class OwnPrizeTools {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IOwnService ownService;

    @Autowired
    private EventTools eventTools;

    public void processPrize(String openid, Prize prize, String reason, ScoreAdditionalDto... dtoList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(openid==null || "".equals(openid)) {return;}
                Account a = accountService.findByOpenid(openid);
                Own own = new Own();
                own.setStatus("0");
                own.setOpenid(openid);
                own.setAccountId(a.getId());
                own.setAccountName(a.getNickname());
                own.setPrizeName(prize.getName());
                own.setStartTime(NormalTools.curDate("yyyy-MM-dd"));
                own.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                own.setCreateLong(System.currentTimeMillis());
                own.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                own.setPrizeId(prize.getId());
                own.setCreateDate(new Date());
                own.setWorth(prize.getWorth());
                own.setPrizeType(prize.getType());
                own.setSource("2");
                own.setSourceInfo(reason);

                ownService.save(own);

                List<TemplateParamDto> pars = new ArrayList<TemplateParamDto>();
                pars.add(new TemplateParamDto("领取人", a.getNickname()));
                pars.add(new TemplateParamDto("礼品", prize.getName()));
                pars.add(new TemplateParamDto("领取时间", NormalTools.curDate("yyyy-MM-dd HH:mm")));
                for(ScoreAdditionalDto dto : dtoList) {
                    pars.add(new TemplateParamDto((dto.getName()==null || "".equals(dto.getName()))?"":dto.getName(), dto.getValue()));
                }
//                eventTools.eventRemind(a.getOpenid(), "积分变化提醒", "积分发生变化啦~~", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/own");
                eventTools.sendTemplateMessage("礼品领取成功通知", openid, "奖品兑换成功", "", "/wx/account/own", (TemplateParamDto[]) pars.toArray());
            }
        }).start();
    }
}
