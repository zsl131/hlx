package com.zslin.wx.controller;

import com.zslin.client.service.IMemberService;
import com.zslin.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/27 17:26.
 * 微信支付控制器
 */
@Controller
@RequestMapping(value = "wx/pay")
public class WeixinPayController {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IMemberLevelService memberLevelService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberChargeService memberChargeService;

    /**
     *  发起微信支付前的准备
     * @param model
     * @param type 1-购买入场券；2-办理会员
     * @param request
     * @return
     */
    @GetMapping(value = "ready")
    public String ready(Model model, String type, HttpServletRequest request) {

        return "weixin/pay/ready";
    }
}
