package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Activity;
import com.zslin.web.service.IActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 22:40.
 */
@Controller
@RequestMapping(value = "admin/activity")
//@AdminAuth(name = "活动管理", psn = "应用管理", orderNum = 4, porderNum = 1, pentity = 0, icon = "fa fa-gamepad")
public class AdminActivityController {

    @Autowired
    private IActivityService activityService;

    @GetMapping(value = "list")
//    @AdminAuth(name = "活动管理", type = "1", orderNum = 1, icon = "fa fa-gamepad")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Activity> datas = activityService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/activity/list";
    }

    @Token(flag= Token.READY)
//    @AdminAuth(name = "添加活动", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("activity", new Activity());
        return "admin/activity/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Activity activity, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            activityService.save(activity);
        }
        return "redirect:/admin/activity/list";
    }

    @Token(flag= Token.READY)
//    @AdminAuth(name="修改活动", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Activity a = activityService.findOne(id);
        model.addAttribute("activity", a);
        return "admin/activity/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Activity activity, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            Activity a = activityService.findOne(id);
            MyBeanUtils.copyProperties(activity, a);
            activityService.save(a);
        }
        return "redirect:/admin/activity/list";
    }

    /*@AdminAuth(name="删除活动", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            activityService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }*/
}
