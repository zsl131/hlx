package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.Feedback;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IFeedbackService;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.EventTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/25 15:05.
 */
@Controller
@RequestMapping(value = "wx/feedback")
public class WeixinFeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private EventTools eventTools;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        Page<Feedback> datas ;
        if(AccountTools.ADMIN.equals(a.getType()) || AccountTools.PARTNER.equals(a.getType())) {
            datas = feedbackService.findAll(SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
            model.addAttribute("isAdmin", "1");
        } else {
            datas = feedbackService.findAll(openid, SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
            model.addAttribute("isAdmin", "0");
        }
        model.addAttribute("datas", datas);
        return "weixin/feedback/list";
    }

    @GetMapping(value = "update")
    public String update(Model model, Integer id, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(AccountTools.ADMIN.equals(a.getType()) || AccountTools.PARTNER.equals(a.getType())) {
            model.addAttribute("feedback", feedbackService.findOne(id));
            model.addAttribute("auth", true);
        } else {
            model.addAttribute("auth", false);
        }

        return "weixin/feedback/update";
    }

    @PostMapping(value = "update")
    public @ResponseBody String update(Integer id, String reply, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(AccountTools.ADMIN.equals(a.getType()) || AccountTools.PARTNER.equals(a.getType())) {
            Feedback f = feedbackService.findOne(id);
            f.setReply(reply);
            f.setReplyDate(new Date());
            f.setReplyLong(System.currentTimeMillis());
            f.setReplyTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            feedbackService.save(f);
            StringBuffer sb = new StringBuffer();
            sb.append("你的反馈：").append(f.getContent()).append(" \\n")
                    .append("回复内容：").append(reply).append("\\n")
                    .append("回复时间：").append(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            eventTools.eventRemind(f.getOpenid(), "在线反馈", "反馈已回复啦", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/account/feedbackList");
            return "1";
        } else {
            return "-1";
        }
    }
}
