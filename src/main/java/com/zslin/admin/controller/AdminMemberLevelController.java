package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.MemberLevel;
import com.zslin.web.service.IMemberLevelService;
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
 * Created by 钟述林 393156105@qq.com on 2017/3/7 11:16.
 */
@Controller
@RequestMapping(value = "admin/memberLevel")
@AdminAuth(name = "会员等级管理", psn = "会员管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-level-up")
public class AdminMemberLevelController {

    @Autowired
    private IMemberLevelService memberLevelService;

    @GetMapping(value = "list")
    @AdminAuth(name = "会员等级管理", type = "1", orderNum = 1, icon = "fa fa-level-up")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<MemberLevel> datas = memberLevelService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("level")));
        model.addAttribute("datas", datas);
        return "admin/memberLevel/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加会员等级", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("memberLevel", new MemberLevel());
        return "admin/memberLevel/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, MemberLevel memberLevel, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            memberLevelService.save(memberLevel);
        }
        return "redirect:/admin/memberLevel/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改会员等级", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        MemberLevel ml = memberLevelService.findOne(id);
        model.addAttribute("memberLevel", ml);
        return "admin/memberLevel/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, MemberLevel memberLevel, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            MemberLevel ml = memberLevelService.findOne(id);
            MyBeanUtils.copyProperties(memberLevel, ml, new String[]{"id"});
            memberLevelService.save(ml);
        }
        return "redirect:/admin/memberLevel/list";
    }
}
