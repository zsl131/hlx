package com.zslin.weixin.controller;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.service.IHlxTicketService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 卡券管理
 */
@RestController
@RequestMapping(value = "weixin/ticket")
public class WeixinTicketController {

    @Autowired
    private IHlxTicketService hlxTicketService;

    @Autowired
    private EventTools eventTools;

    /** 查询电子卡券 */
    @GetMapping(value = "queryTicket")
    public String queryTicket(String ticketNo) {
        HlxTicket ticket = hlxTicketService.findByTicketNo(ticketNo);

        if(ticket==null) {
            return "【"+ticketNo+"】不存在";
        }
        if("1".equals(ticket.getStatus())) {
            return "【"+ticketNo+"】已经于["+ticket.getUseTime()+"]核销";
        }
        return "1"; //返回1表示可以核销
    }

    /**
     * 核销卡券
     * @param ticketNo 卡券编号
     * @return
     */
    @GetMapping(value = "writeOff")
    public String writeOff(String ticketNo) {
        HlxTicket ticket = hlxTicketService.findByTicketNo(ticketNo);
        if(ticket==null) {
            return "【"+ticketNo+"】不存在";
        }
        if("1".equals(ticket.getStatus())) {
           return "【"+ticketNo+"】已经于["+ticket.getUseTime()+"]核销";
        }
        ticket.setUseDay(NormalTools.curDate());
        ticket.setUseTime(NormalTools.curDatetime());
        ticket.setUseLong(System.currentTimeMillis());
        ticket.setStatus("1");

        hlxTicketService.save(ticket);

        eventTools.eventRemind(ticket.getOpenid(), "你的卡券核销啦", "卡券核销提醒",
                DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您的卡券【"+ticketNo+"】已于【"+ticket.getUseTime()+"】成功核销", "");

        return "1";
    }
}
