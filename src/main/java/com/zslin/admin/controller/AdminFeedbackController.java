package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Feedback;
import com.zslin.web.service.IFeedbackService;
import com.zslin.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:35.
 */
@Controller
@RequestMapping(value = "admin/feedback")
@AdminAuth(name = "反馈管理", psn = "应用管理", orderNum = 8, porderNum = 1, pentity = 0, icon = "fa fa-commenting")
public class AdminFeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private EventTools eventTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "反馈管理", type = "1", orderNum = 1, icon = "fa fa-commenting")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Feedback> datas = feedbackService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/feedback/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改微信反馈", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method= RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Feedback f = feedbackService.findOne(id);
        model.addAttribute("feedback", f);
        return "admin/feedback/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Feedback feedback, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Feedback f = feedbackService.findOne(id);
            f.setReply(feedback.getReply());
            f.setStatus(feedback.getStatus());
            f.setReplyDate(new Date());
            f.setReplyLong(System.currentTimeMillis());
            f.setReplyTime(DateTools.formatDate(new Date()));
            feedbackService.save(f);

            if("text".equals(f.getType())) {
                eventTools.eventRemind(f.getOpenid(), "反馈得到回复了",
                        "反馈回复", f.getReplyTime(), "你的反馈：" + f.getContent() + "\\n回复内容：" + f.getReply(), "/wx/account/feedbackList");
            } else if("image".equals(f.getType())) {
                eventTools.eventRemind(f.getOpenid(), "反馈得到回复了",
                        "反馈回复", f.getReplyTime(), "你反馈的图片信息得到回复：\\n" + f.getReply(), "/wx/account/feedbackList");
            }

        }
        return "redirect:/admin/feedback/list";
    }
}
