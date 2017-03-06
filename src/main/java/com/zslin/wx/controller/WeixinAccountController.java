package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.web.model.*;
import com.zslin.web.service.*;
import com.zslin.wx.tools.QrTools;
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

    @Autowired
    private IQrcodeService qrcodeService;

    @Autowired
    private QrTools qrTools;

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

    @GetMapping(value = "getQr")
    public String getQr(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a!=null) {
            Qrcode qr = qrcodeService.findByOpenid(openid);
            if(qr==null) {
                String path = qrTools.genUserQr(a.getId() + "", a.getHeadimgurl());
                Qrcode qrcode = new Qrcode();
                qrcode.setOpenid(openid);
                qrcode.setAccountId(a.getId());
                qrcode.setNickname(a.getNickname());
                qrcode.setQrPath(path);
                qrcode.setName(a.getNickname());
                qrcode.setHeadimg(a.getHeadimgurl());
                qrcode.setCreateDate(new Date());
                qrcode.setCreateLong(System.currentTimeMillis());
                qrcode.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                qrcode.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                qr = qrcodeService.findByOpenid(openid);
                if(qr==null) {
                    qrcodeService.save(qrcode);
                }
            }
            return "redirect:/weixin/qr?id="+a.getId();
        }
        return "redirect:/weixin/index";
    }

    @PostMapping(value = "updateQrName")
    public @ResponseBody String updateQrName(String name, HttpServletRequest request) {
        if(name!=null && !"".equals(name.trim())) {
            String openid = SessionTools.getOpenid(request);
            qrcodeService.updateName(name, openid);
        }
        return "1";
    }
}
