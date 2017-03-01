package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.ScoreRule;
import com.zslin.web.service.IScoreRuleService;
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
 * Created by 钟述林 393156105@qq.com on 2017/3/1 16:30.
 */
@Controller
@RequestMapping(value = "admin/scoreRule")
@AdminAuth(name = "积分规则管理", psn = "应用管理", orderNum = 4, porderNum = 1, pentity = 0, icon = "fa fa-server")
public class AdminScoreRuleController {

    @Autowired
    private IScoreRuleService scoreRuleService;

    @GetMapping(value = "list")
    @AdminAuth(name = "积分规则管理", type = "1", orderNum = 1, icon = "fa fa-server")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<ScoreRule> datas = scoreRuleService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id")));
        model.addAttribute("datas", datas);
        return "admin/scoreRule/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加积分规则", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("scoreRule", new ScoreRule());
        return "admin/scoreRule/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, ScoreRule scoreRule, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            ScoreRule sr = scoreRuleService.findByCode(scoreRule.getCode());
            if(sr!=null) {
                throw new SystemException("规则编码【"+scoreRule.getCode()+"】已经存在");
            }
            scoreRuleService.save(scoreRule);
        }
        return "redirect:/admin/scoreRule/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改积分规则", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        ScoreRule sr = scoreRuleService.findOne(id);
        model.addAttribute("scoreRule", sr);
        return "admin/scoreRule/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, ScoreRule scoreRule, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            ScoreRule sr = scoreRuleService.findOne(id);
            MyBeanUtils.copyProperties(scoreRule, sr, new String[]{"code"});
            scoreRuleService.save(sr);
        }
        return "redirect:/admin/scoreRule/list";
    }
}
