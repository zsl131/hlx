package com.zslin.weixin.controller;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.weixin.dto.JsonResult;
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
    public JsonResult queryTicket(String ticketNo, String storeSn) {
        //System.out.println("===================="+ticketNo+"==========="+storeSn);
        HlxTicket ticket = hlxTicketService.findByTicketNo(ticketNo);

        if(ticket==null) {
            return JsonResult.error("1001", "【"+ticketNo+"】不存在");
        }
        if("1".equals(ticket.getStatus())) {
            return JsonResult.error("1002", "【"+ticketNo+"】已经于["+ticket.getUseTime()+"]核销");
        }
        if("2".equals(ticket.getStatus())) {
            return JsonResult.error("1004", "【"+ticketNo+"】已过期");
        }
        if("3".equals(ticket.getStatus())) {
            return JsonResult.error("1005", "【"+ticketNo+"】已作废");
        }
        if(storeSn!=null && !ticket.getStoreSn().equals(storeSn)) {
            return JsonResult.error("1003", "【"+ticketNo+"】不可在该店铺使用");
        }
        return JsonResult.success().set("ticket", ticket);
    }

    /**
     * 核销卡券
     * @param ticketNo 卡券编号
     * @return
     */
    @GetMapping(value = "writeOff")
    public JsonResult writeOff(String ticketNo) {
        HlxTicket ticket = hlxTicketService.findByTicketNo(ticketNo);
        if(ticket==null) {
            return JsonResult.error("1001", "【"+ticketNo+"】不存在");
        }
        if("1".equals(ticket.getStatus())) {
            return JsonResult.error("1002", "【"+ticketNo+"】已经于["+ticket.getUseTime()+"]核销");
        }
        ticket.setUseDay(NormalTools.curDate());
        ticket.setUseTime(NormalTools.curDatetime());
        ticket.setUseLong(System.currentTimeMillis());
        ticket.setStatus("1");

        hlxTicketService.save(ticket);

        eventTools.eventRemind(ticket.getOpenid(), "你的卡券核销啦", "卡券核销提醒",
                DateTools.date2Str(new Date(), "yyyy-MM-dd"), "您的卡券【"+ticketNo+"】已于【"+ticket.getUseTime()+"】成功核销", "");

        return JsonResult.success().set("ticket", ticket);
    }
}
