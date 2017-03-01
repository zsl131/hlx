package com.zslin.wx.controller;

import com.zslin.web.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //微信用户个人中心
    @GetMapping(value = "me")
    public String me(Model model, HttpServletRequest request) {

        return "weixin/account/me";
    }
}
