package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Comment;
import com.zslin.web.model.Feedback;
import com.zslin.web.service.ICommentService;
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
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:20.
 */
@Controller
@RequestMapping(value = "admin/comment")
@AdminAuth(name = "点评管理", psn = "应用管理", orderNum = 7, porderNum = 1, pentity = 0, icon = "fa fa-comment")
public class AdminCommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private EventTools eventTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "点评管理", type = "1", orderNum = 1, icon = "fa fa-comment")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Comment> datas = commentService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/comment/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改点评", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method= RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Comment c = commentService.findOne(id);
        model.addAttribute("comment", c);
        return "admin/comment/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Comment comment, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Comment c = commentService.findOne(id);
            c.setReply(comment.getReply());
            c.setStatus(comment.getStatus());
            c.setReplyDate(new Date());
            c.setReplyLong(System.currentTimeMillis());
            c.setReplyTime(DateTools.formatDate(new Date()));
            commentService.save(c);

            eventTools.eventRemind(c.getOpenid(), "点评得到回复了",
                    "点评回复", c.getReplyTime(), "菜品名称："+c.getFoodName()+"\\n你的点评："+c.getContent()+"\\n回复内容："+c.getReply(), "/wx/food/detail?id="+c.getFoodId());
        }
        return "redirect:/admin/comment/list";
    }
}
