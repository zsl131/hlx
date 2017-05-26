package com.zslin.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:13.
 */
@Controller
@RequestMapping(value = "wx/cal")
public class WeixinCalController {

    @GetMapping(value = "index")
    public String index(Model model, String amount, HttpServletRequest request) {
        model.addAttribute("amount", amount);
        return "weixin/cal/index";
    }
}
