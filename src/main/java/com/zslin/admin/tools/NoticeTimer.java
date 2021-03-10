package com.zslin.admin.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.HlxTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/19.
 * 通知定时器，通知管理员和股东营业信息
 */
@Component
public class NoticeTimer {

    @Autowired
    private HlxTools hlxTools;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private AccountTools accountTools;

    //每个月1号0点自动统计
    @Scheduled(cron = "0 0 0 1 * ?")
    public void timerCron() {
        //通知对象：管理员与股东
        List<String> openids = accountTools.getOpenid(AccountTools.ADMIN, AccountTools.PARTNER);
        String content = hlxTools.queryFinanceByTimer(ClientFileTools.HLX_SN);
        eventTools.eventRemind(openids, "汉丽轩-营业通知", "自动", DateTools.date2Str(new Date(), "yyyyMMdd"), content, "");

        String contentQwzw = hlxTools.queryFinanceByTimer(ClientFileTools.QWZW_SN);
        //只发部分人员
        eventTools.eventRemind(AccountTools.defaultAdmins(), "签王之王-营业通知", "自动", DateTools.date2Str(new Date(), "yyyyMMdd"), contentQwzw, "");
//        System.out.println("current time : "+ sdf.format(new Date()));
//        System.out.println(content);
    }

    //每天23点自动统计
    @Scheduled(cron = "0 0 23 * * ?")
    public void dataByDay() {
        //通知对象：管理员与股东
        List<String> openids = accountTools.getOpenid(AccountTools.ADMIN, AccountTools.PARTNER);
        String content = hlxTools.calDay(ClientFileTools.HLX_SN);
        eventTools.eventRemind(openids, "汉丽轩-当天营业通知", "自动统计", DateTools.date2Str(new Date(), "yyyyMMdd"), content, "");

        String contentQwzw = hlxTools.calDay(ClientFileTools.QWZW_SN);
        //只发部分人员
        eventTools.eventRemind(AccountTools.defaultAdmins(), "签王之王-当天营业通知", "自动统计", DateTools.date2Str(new Date(), "yyyyMMdd"), contentQwzw, "");
//        System.out.println("current time : "+ sdf.format(new Date()));
//        System.out.println(content);
    }
}
