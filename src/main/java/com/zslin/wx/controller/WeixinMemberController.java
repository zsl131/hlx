package com.zslin.wx.controller;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.web.model.MemberLevel;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IMemberChargeService;
import com.zslin.web.service.IMemberLevelService;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 16:44.
 * 微信会员
 */
@Controller
@RequestMapping(value = "wx/member")
public class WeixinMemberController {

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IMemberChargeService memberChargeService;

    @Autowired
    private IAccountService accountService;

    /** 会员等级 */
    @GetMapping(value = "level")
    public String level(Model model, HttpServletRequest request) {
        SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("status", "eq", "1");
        List<MemberLevel> datas = memberLevelService.findAll(builder.generate(), SimpleSortBuilder.generateSort("level"));
        model.addAttribute("datas", datas);
        return "weixin/member/level";
    }

    @GetMapping(value = "charge")
    public String charge(Integer id, Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        model.addAttribute("account", accountService.findByOpenid(openid)); //
        model.addAttribute("level", memberLevelService.findOne(id));
        return "weixin/member/charge";
    }
}
