package com.zslin.wx.controller;

import com.zslin.wx.tools.SessionTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/7 22:45.
 */
@RestController
@RequestMapping(value = "wx/test")
public class WxTestController {

    @GetMapping(value = "index")
    public String index(HttpServletRequest request) {

        System.out.println("========="+ SessionTools.getOpenid(request));
        return "Hello Weixin";
    }
}
