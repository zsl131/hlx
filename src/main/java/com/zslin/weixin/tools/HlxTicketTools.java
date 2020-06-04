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

        Integer count3 = hlxTicketService.queryByDay("2020-06-04");
        Integer count4 = hlxTicketService.queryWriteOffCount("2020-06-04");

        String sep = "\\n";

        StringBuffer sb = new StringBuffer();
        sb.append("领券总数：").append(count1).append(" 张")
                .append(sep).append("总核销数：").append(count2).append(" 张")
                .append(sep).append("今日领券：").append(count3).append(" 张")
                .append(sep).append("今日核销").append(count4).append(" 张");
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

    public void receiveTicket10(String openid) {
        String name = "10元抵价券";
        List<HlxTicket> ticketList = hlxTicketService.findByOpenid(openid);
        if(ticketList!=null && ticketList.size()>0) {
            HlxTicket ticket = ticketList.get(0);
            String status = ("0".equals(ticket.getStatus())?"待使用":"已使用");
            eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您已领取"+name+"，编号【"+ticket.getTicketNo()+"】，状态【"+status+"】", "");
        } else {
            Account a = accountService.findByOpenid(openid);
            if(a!=null) {
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

                hlxTicketService.save(t);

                eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                        DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您已成功领取"+name+"，编号【"+t.getTicketNo()+"】，状态【待使用】", "");
            }
        }
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
