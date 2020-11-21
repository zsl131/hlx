package com.zslin.weixin.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.sms.tools.RandomTools;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.weixin.dto.TicketDto;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.model.OrderTicket;
import com.zslin.weixin.service.IHlxTicketService;
import com.zslin.weixin.service.IOrderTicketService;
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
    private IOrderTicketService orderTicketService;

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
        TicketDto dto = buildDto(scene);
        /*if("10".equals(scene)) {
            receiveTicket10(openid);
        }*/
        //System.out.println("---HlxTicketTools.receiveTicket---"+dto);
        if(dto.getWorth()==10) { //如果是10元券
            receiveTicket10(openid);
        } else if(isOrderTicket(dto)) { //如果是汉丽轩订单领券
            buildOrderTicket(dto, openid);
        }
    }

    private void buildOrderTicket(TicketDto dto, String openid) {
        OrderTicket ot = orderTicketService.findByOrderNo(dto.getSn());
        if(ot==null || !"0".equals(ot.getStatus())) { //不存在，或当前状态不可领券
            eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                    //"您已领取"+name+"，编码【"+ticket.getTicketNo()+"】，状态【"+status+"】"
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), "此领券码已失效或已被领取", "/wx/account/ticket");
        } else {
            String sn = "hlx-order"; //TODO 此券SN
            orderTicketService.updateStatus("1", dto.getSn());
            Account a = accountService.findByOpenid(openid);
            if(a!=null) {
                Float worth = 100f; //价值
                Float reach = 188f; //
                String name = "汉丽轩-100元抵价券";
                HlxTicket t = new HlxTicket();
                t.setAccountId(a.getId());
                t.setCreateDay(NormalTools.curDate());
                t.setCreateLong(System.currentTimeMillis());
                t.setCreateTime(NormalTools.curDatetime());
                t.setNickname(a.getNickname());
                t.setOpenid(a.getOpenid());
                t.setStatus("0");
                t.setTicketName(name);
                t.setTicketWorth(worth);
                t.setStoreSn("hlx");
                t.setFromStoreSn("hlx");
                t.setSn(sn);

                t.setTicketNo(buildCode()); //设置编号
                t.setType("10"); //电子券
                t.setUseType("2"); //满减
                t.setReachMoney(reach); //满reach元可使用
                //TODO 需要设置有效期

                hlxTicketService.save(t);

                eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                        DateTools.date2Str(new Date(), "yyyy-MM-dd"), buildTicketCon(name, t.getTicketNo(), "待使用", reach, worth), "/wx/account/ticket");
            }
        }
    }

    private boolean isOrderTicket(TicketDto dto) {
        return (dto.getWorth()==100 && dto.getSn().length()==13); //后面是订单号
    }

    //scene: 10_hlx
    private TicketDto buildDto(String scene) {
        TicketDto dto = new TicketDto();
        String [] array = scene.split("_"); //下划线分隔
        Float worth = Float.parseFloat(array[0]); //第一个是金额
        String sn = "";
        if(array.length>=2) {sn = array[1];}
        if(sn==null ||"".equals(sn)) {sn = "hlx";} //默认为hlx
        dto.setSn(sn);
        dto.setWorth(worth);
        return dto;
    }

    private void receiveTicket10(String openid) {
        String name = "10元抵价券";
        List<HlxTicket> ticketList = hlxTicketService.findByOpenid(openid);
        if(ticketList!=null && ticketList.size()>0) {
            HlxTicket ticket = ticketList.get(0);
            String status = ("0".equals(ticket.getStatus())?"待使用":"已使用");
            eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                    //"您已领取"+name+"，编码【"+ticket.getTicketNo()+"】，状态【"+status+"】"
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), buildTicketCon(name, ticket.getTicketNo(), status, ticket.getReachMoney(), ticket.getTicketWorth()), "");
        } else {
            Account a = accountService.findByOpenid(openid);
            if(a!=null) {
                String sn = "hlx-ticket";
                Float reach = 99f; //
                Float worth = 10f;
                HlxTicket t = new HlxTicket();
                t.setAccountId(a.getId());
                t.setCreateDay(NormalTools.curDate());
                t.setCreateLong(System.currentTimeMillis());
                t.setCreateTime(NormalTools.curDatetime());
                t.setNickname(a.getNickname());
                t.setOpenid(a.getOpenid());
                t.setStatus("0");
                t.setTicketName(name);
                t.setTicketWorth(worth);
                t.setTicketNo(buildCode()); //设置编号
                t.setType("10"); //电子券
                t.setUseType("2"); //满减
                t.setSn(sn);
                t.setReachMoney(reach); //满99元可使用

                hlxTicketService.save(t);

                eventTools.eventRemind(openid, "领券提醒", "领券提醒",
                        DateTools.date2Str(new Date(), "yyyy-MM-dd"), buildTicketCon(name, t.getTicketNo(), "待使用", reach, worth), "");
            }
        }
    }

    private String buildTicketCon(String name, String no, String status, Float reach, Float worth) {
        String sep = "\\n";
        StringBuffer sb = new StringBuffer();
        sb.append("您已成功领取").append(name).append(sep)
                .append("编码：【").append(no).append("】").append(sep)
                .append("状态：【").append(status).append("】").append(sep)
                .append("满【").append(reach).append("】元可抵【").append(worth).append("】").append(sep)
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
