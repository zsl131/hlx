package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.web.model.Account;
import com.zslin.web.model.Comment;
import com.zslin.web.model.Feedback;
import com.zslin.web.model.WalletDetail;
import com.zslin.web.service.*;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 17:13.
 */
@Controller
@RequestMapping(value = "wx/account")
public class WeixinAccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IOwnService ownService;

    @Autowired
    private IWalletDetailService walletDetailService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IFeedbackService feedbackService;

    //微信用户个人中心
    @GetMapping(value = "me")
    public String me(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account account = accountService.findByOpenid(openid);
        model.addAttribute("account", account);
        model.addAttribute("wallet", walletService.findByOpenid(openid));
        model.addAttribute("pullCount", accountService.findPullCount(account.getId()));
        model.addAttribute("ownCount", ownService.findCount(openid)); //礼物数量
        model.addAttribute("commentCount", commentService.findCount(openid)); //评论数量
        model.addAttribute("feedbackCount", feedbackService.findCount(openid)); //消息数量
        return "weixin/account/me";
    }

    @GetMapping(value = "score")
    public String score(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        builder.add("type", "eq", "2");
        model.addAttribute("wallet", walletService.findByOpenid(openid));
        Page<WalletDetail> datas = walletDetailService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/score";
    }

    @GetMapping(value = "commentList")
    public String commentList(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<Comment> datas = commentService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/commentList";
    }

    @GetMapping(value = "feedbackList")
    public String feedbackList(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("openid", "eq", openid);
        Page<Feedback> datas = feedbackService.findAll(builder.generate(),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "weixin/account/feedbackList";
    }
}
