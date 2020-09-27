package com.zslin.weixin.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.sms.tools.RandomTools;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.service.IHlxTicketService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 10元优惠券工具类
 */
@Component
public class HlxTicketTools {

    @Autowired
    private IHlxTicketService hlxTicketService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private EventTools eventTools;

    /**
     * 生成事件统计内容
     * @return
     */
    public String buildEventCalContent() {
        String curDay = NormalTools.curDate("yyyy-MM-dd"); //当前日期

        Integer count1 = hlxTicketService.queryAll();
        Integer count2 = hlxTicketService.queryAll("1");

        Integer count3 = hlxTicketService.queryByDay(curDay);
        Integer count4 = hlxTicketService.queryWriteOffCount(curDay);

        String sep = "\\n";

        StringBuffer sb = new StringBuffer();
        sb.append("领券总数：").append(count1).append(" 张")
                .append(sep).append("总核销数：").append(count2).append(" 张")
                .append(sep).append("今日领券：").append(count3).append(" 张")
                .append(sep).append("今日核销：").append(count4).append(" 张");
        return sb.toString();
    }

    /**
     * 收到券码
     * @param openid openid
     * @param scene 对应值，如：10
     */
    public void receiveTicket(String openid, String scene) {
        if("10".equals(scene)) {
            receiveTicket10(openid);
        }
    }

    private void receiveTicket10(String openid) {
        String name = "10元抵价券";
        List<HlxTicket> ticketList = hlxTicketService.findByOpenid(openid);
        if(ticketList!=null && ticketList.size()>0) {
            HlxTicket ticket = ticketList.get(0);
            String status = ("0".equals(ticket.getStatus())?"待使用":"已使用");
            eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                    //"您已领取"+name+"，编码【"+ticket.getTicketNo()+"】，状态【"+status+"】"
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), buildTicketCon(name, ticket.getTicketNo(), status, ticket.getTicketWorth()), "");
        } else {
            Account a = accountService.findByOpenid(openid);
            if(a!=null) {
                Integer worth = 99; //
                HlxTicket t = new HlxTicket();
                t.setAccountId(a.getId());
                t.setCreateDay(NormalTools.curDate());
                t.setCreateLong(System.currentTimeMillis());
                t.setCreateTime(NormalTools.curDatetime());
                t.setNickname(a.getNickname());
                t.setOpenid(a.getOpenid());
                t.setStatus("0");
                t.setTicketName(name);
                t.setTicketWorth(10);
                t.setTicketNo(buildCode()); //设置编号
                t.setType("10"); //电子券
                t.setUseType("2"); //满减
                t.setReachMoney(worth); //满99元可使用

                hlxTicketService.save(t);

                eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                        DateTools.date2Str(new Date(), "yyyy-MM-dd"), buildTicketCon(name, t.getTicketNo(), "待使用", worth), "");
            }
        }
    }

    private String buildTicketCon(String name, String no, String status, Integer worth) {
        String sep = "\\n";
        StringBuffer sb = new StringBuffer();
        sb.append("您已成功领取").append(name).append(sep)
                .append("编码：【").append(no).append("】").append(sep)
                .append("状态：【").append(status).append("】").append(sep)
                .append("满【").append(worth).append("】元可使用").append(sep)
                .append("下单前把此编码告知收银员即可。");
        return sb.toString();
    }

    private String buildCode() {
        String code = "";
        HlxTicket ticket;
        do {
            code = RandomTools.genCode7();
            ticket = hlxTicketService.findByTicketNo(code);
            if(ticket==null) {break;}
        } while(ticket!=null);
        return code;
    }
}
