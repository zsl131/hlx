package com.zslin.basic.tools;

import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.JsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动通知工具类
 */
@Component
public class AutoNoticeTools {

    @Autowired
    private EventTools eventTools;

    @Autowired
    private IAccountService accountService;

    private static final String SEP = "\\n";

    public void notice(String params) {
        String name = JsonTools.getJsonParam(params, "name");
        String phone = JsonTools.getJsonParam(params, "phone");
        String msg = JsonTools.getJsonParam(params, "msg");

        String openid = accountService.findOpenidByPhone(phone);
        if(openid==null || "".equals(openid)) {
            noticeAdmin(name, phone);
        } else {
            eventTools.eventRemind(openid, "今日工作完成度提醒", "今天的工作你完成了吗？",
                    DateTools.date2Str(new Date(), "yyyy-MM-dd"), msg, "");
        }
    }

    private void noticeAdmin(String name, String phone) {
        String openid = accountService.findOpenidByPhone("15925061256");
        eventTools.eventRemind(openid, "管理员未绑定通知", "有管理员未绑定公众号",
            DateTools.date2Str(new Date(), "yyyy-MM-dd"), "姓名：【"+name+"】"+SEP+"电话：【"+phone+"】", "");
    }

//    eventTools.eventRemind(openid, "领券提醒", "领券提醒",
//            "您已领取"+name+"，编码【"+ticket.getTicketNo()+"】，状态【"+status+"】"
//            DateTools.date2Str(new Date(), "yyyy-MM-dd"), "此领券码已失效或已被领取", "/wx/account/ticket");
}
