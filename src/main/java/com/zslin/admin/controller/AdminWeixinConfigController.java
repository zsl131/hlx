package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.web.model.WeixinConfig;
import com.zslin.web.service.IWeixinConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/weixinConfig")
@AdminAuth(name = "微信配置管理", psn = "微信管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-cog")
public class AdminWeixinConfigController {

    @Autowired
    private IWeixinConfigService weixinConfigService;

    @AdminAuth(name="微信配置管理", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        WeixinConfig config = weixinConfigService.loadOne();
        if(config==null) {config = new WeixinConfig();}
        model.addAttribute("weixinConfig", config);
        return "admin/weixinConfig/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, WeixinConfig weixinConfig, HttpServletRequest request) {
        WeixinConfig wc = weixinConfigService.loadOne();
        if(wc==null) {
            weixinConfigService.save(weixinConfig);
        } else {
            MyBeanUtils.copyProperties(weixinConfig, wc, new String[]{"id"});
            weixinConfigService.save(wc);
        }

        return "redirect:/admin/weixinConfig/index";
    }
}
