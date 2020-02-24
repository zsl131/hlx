package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/store")
@AdminAuth(name="店铺管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminStoreController {

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "店铺管理", orderNum = 1, type = "1", icon = "fa fa-bank")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Store> datas = storeDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/multi/store/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加店铺", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("store", new Store());
        return "admin/multi/store/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Store store, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            Store s = storeDao.findBySn(store.getSn());
            if(s!=null) {
                throw new SystemException("店铺编号【"+store.getSn()+"】已经存在");
            }
            storeDao.save(store);
        }
        return "redirect:/admin/store/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改店铺信息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Store s = storeDao.findOne(id);
        model.addAttribute("store", s);
        return "admin/multi/store/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Store store, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Store s = storeDao.findOne(id);
            MyBeanUtils.copyProperties(store, s);
            storeDao.save(s);
        }
        return "redirect:/admin/store/list";
    }

    @AdminAuth(name="删除店铺", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            storeDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
