package com.zslin.finance.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.ISupplierDao;
import com.zslin.finance.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/supplier")
@AdminAuth(name="供货商管理", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminSupplierController {

    @Autowired
    private ISupplierDao supplierDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "供货商管理", orderNum = 1, type = "1", icon = "fa fa-list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Supplier> datas = supplierDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/finance/supplier/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加供货商", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("supplier", new Supplier());

        return "admin/finance/supplier/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Supplier supplier, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            supplier.setCreateDay(NormalTools.curDate());
            supplier.setCreateTime(NormalTools.curDatetime());
            supplier.setCreateLong(System.currentTimeMillis());
            supplierDao.save(supplier);
        }
        return "redirect:/admin/supplier/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改供货商信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Supplier s = supplierDao.findOne(id);
        model.addAttribute("supplier", s);

        return "admin/finance/supplier/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Supplier supplier, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Supplier s = supplierDao.findOne(id);
            MyBeanUtils.copyProperties(supplier, s, "id");
            supplierDao.save(s);
        }
        return "redirect:/admin/supplier/list";
    }

    @AdminAuth(name="删除供货商", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            supplierDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
