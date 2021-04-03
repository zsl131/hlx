package com.zslin.finance.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinanceCategoryDao;
import com.zslin.finance.model.FinanceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/financeCategory")
@AdminAuth(name="财务类别管理", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminFinanceCategoryController {

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "财务类别管理", orderNum = 1, type = "1", icon = "fa fa-desktop")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<FinanceCategory> datas = financeCategoryDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/finance/financeCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加类别", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("financeCategory", new FinanceCategory());

        return "admin/finance/financeCategory/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, FinanceCategory category, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            category.setFirstSpell(PinyinToolkit.cn2FirstSpell(category.getName()));
            category.setLongSpell(PinyinToolkit.cn2Spell(category.getName(), ""));
            financeCategoryDao.save(category);
        }
        return "redirect:/admin/financeCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改类别信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        FinanceCategory c = financeCategoryDao.findOne(id);
        model.addAttribute("category", c);

        return "admin/finance/financeCategory/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, FinanceCategory category, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            FinanceCategory c = financeCategoryDao.findOne(id);
            MyBeanUtils.copyProperties(category, c, "id");
            c.setFirstSpell(PinyinToolkit.cn2FirstSpell(category.getName()));
            c.setLongSpell(PinyinToolkit.cn2Spell(category.getName(), ""));
            financeCategoryDao.save(c);
        }
        return "redirect:/admin/financeCategory/list";
    }

    @AdminAuth(name="删除类别", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            financeCategoryDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
