package com.zslin.wx.controller;

import com.zslin.wx.tools.SessionTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/7 22:45.
 */
@RestController
@RequestMapping(value = "weixin/test")
public class WxTestController {

    @GetMapping(value = "index")
    public String index(HttpServletRequest request) {

        System.out.println("========="+ SessionTools.getOpenid(request));
        return "Hello Weixin";
    }

    @GetMapping(value = "openid")
    public @ResponseBody String openid(String from, HttpServletRequest request) {
        //如果from为1，则表示是服务器上的正式Openid，否则则是本机的Openid
        String o = "1".equals(from)?"o_TZkwbz0pzuCTmrWqMGNHriMHTo":"orLIDuFuyeOygL0FBIuEilwCF1lU";
        SessionTools.setOpenid(request, o);
        return "ok";
    }
}
