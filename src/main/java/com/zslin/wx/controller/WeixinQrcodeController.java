package com.zslin.wx.controller;

import com.zslin.web.model.Qrcode;
import com.zslin.web.service.IQrcodeService;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/6 8:48.
 */
@Controller
@RequestMapping(value = "weixin")
public class WeixinQrcodeController {

    @Autowired
    private IQrcodeService qrcodeService;

    @GetMapping(value = "qr")
    public String qr(Model model, Integer id, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Qrcode qr = null;
        if(id!=null && id>0) {
            qr = qrcodeService.findByAccountId(id);
        } else {
            qr = qrcodeService.findByOpenid(openid);
        }
        if(qr!=null && openid!=null && qr.getOpenid().equals(openid)) {
            model.addAttribute("canModify", true);
        } else {
            model.addAttribute("canModify", false);
        }
        model.addAttribute("qrcode", qr);
        return "weixin/qr";
    }
}
