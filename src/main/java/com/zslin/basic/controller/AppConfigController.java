package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.model.AppConfig;
import com.zslin.basic.service.IAppConfigService;
import com.zslin.basic.tools.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统配置
 * @author zslin.com 20160519
 *
 */
@Controller
@RequestMapping(value="admin/appConfig")
@AdminAuth(name="系统配置", orderNum=10, psn="系统管理", pentity=0, porderNum=20)
public class AppConfigController {

    @Autowired
    private IAppConfigService appConfigService;

    @AdminAuth(name="系统配置", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("appConfig", appConfigService.loadOne());
        return "admin/basic/appConfig/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, AppConfig appConfig, HttpServletRequest request) {

        AppConfig a = appConfigService.loadOne();
        if(a==null) {
            appConfigService.save(appConfig);
        } else {
            MyBeanUtils.copyProperties(appConfig, a, new String[]{"id"});
            appConfigService.save(a);
        }

        request.getSession().setAttribute("appConfig", appConfig); //修改后需要修改一次Session中的值
        return "redirect:/admin/appConfig/index";
    }
}