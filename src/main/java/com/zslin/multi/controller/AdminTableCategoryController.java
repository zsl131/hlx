package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.dao.ITableCategoryDao;
import com.zslin.multi.model.Store;
import com.zslin.multi.model.TableCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:11.
 */
@Controller
@RequestMapping(value = "admin/tableCategory")
@AdminAuth(name = "餐桌分类管理", psn = "排队管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-tasks")
public class AdminTableCategoryController {

    @Autowired
    private ITableCategoryDao tableCategoryDao;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "餐桌分类管理", orderNum = 1, type = "1", icon = "fa fa-tasks")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<TableCategory> datas = tableCategoryDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/tableCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加餐桌分类", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("category", new TableCategory());
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/tableCategory/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, TableCategory category, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            tableCategoryDao.save(category);
        }
        return "redirect:/admin/tableCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改餐桌分类", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        TableCategory c = tableCategoryDao.findOne(id);
        model.addAttribute("category", c);
        return "admin/tableCategory/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, TableCategory category, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            TableCategory c = tableCategoryDao.findOne(id);
            MyBeanUtils.copyProperties(category, c, new String[]{"id"});

            tableCategoryDao.save(c);
        }
        return "redirect:/admin/tableCategory/list";
    }

    @AdminAuth(name="删除餐桌分类", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            tableCategoryDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
