package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.web.model.Rules;
import com.zslin.web.service.IRulesService;
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
@RequestMapping(value = "admin/rules")
@AdminAuth(name = "全局配置管理", psn = "应用管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-cog")
public class AdminRulesController {

    @Autowired
    private IRulesService rulesService;

    @AdminAuth(name="全局配置管理", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Rules rules = rulesService.loadOne();
        if(rules==null) {rules = new Rules();}
        model.addAttribute("rules", rules);
        return "admin/rules/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, Rules rules, HttpServletRequest request) {

        Rules r = rulesService.loadOne();
        if(r==null) {
            rulesService.save(rules);
        } else {
            MyBeanUtils.copyProperties(rules, r, new String[]{"id"});
            rulesService.save(r);
        }

        return "redirect:/admin/rules/index";
    }
}
