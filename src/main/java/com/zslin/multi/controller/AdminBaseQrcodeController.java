package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IBaseQrcodeDao;
import com.zslin.multi.model.BaseQrcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/baseQrcode")
@AdminAuth(name="二维码管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminBaseQrcodeController {

    @Autowired
    private IBaseQrcodeDao baseQrcodeDao;


    @GetMapping(value = "list")
    @AdminAuth(name = "二维码列表", orderNum = 1, type = "1", icon = "fa fa-qrcode")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<BaseQrcode> datas = baseQrcodeDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/multi/baseQrcode/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加二维码", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("baseQrcode", new BaseQrcode());
        return "admin/multi/baseQrcode/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, BaseQrcode baseQrcode, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            baseQrcodeDao.save(baseQrcode);
        }
        return "redirect:/admin/baseQrcode/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改二维码", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        BaseQrcode b = baseQrcodeDao.findOne(id);
        model.addAttribute("baseQrcode", b);
        return "admin/multi/baseQrcode/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, BaseQrcode baseQrcode, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            BaseQrcode b = baseQrcodeDao.findOne(id);
            b.setName(baseQrcode.getName());
            b.setUrl(baseQrcode.getUrl());
            baseQrcodeDao.save(b);
        }
        return "redirect:/admin/baseQrcode/list";
    }

    @AdminAuth(name="删除二维码", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            baseQrcodeDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
