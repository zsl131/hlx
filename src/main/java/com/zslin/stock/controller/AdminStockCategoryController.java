package com.zslin.stock.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.model.StockCategory;
import com.zslin.stock.service.IStockCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/5/21.
 * 库存管理-分类管理
 */
@Controller
@RequestMapping(value = "admin/stockCategory")
@AdminAuth(name = "分类管理", psn = "库存管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-folder-open")
public class AdminStockCategoryController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "库存分类管理", orderNum = 1, type = "1", icon = "fa fa-cubes")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<StockCategory> datas = stockCategoryService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id")));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/stock/stockCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加库存分类", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("stockCategory", new StockCategory());
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/stock/stockCategory/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, StockCategory stockCategory, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            stockCategory.setNameFull(PinyinToolkit.cn2Spell(stockCategory.getName(), ""));
            stockCategory.setNameShort(PinyinToolkit.cn2FirstSpell(stockCategory.getName()));
            Store store = storeDao.findBySn(stockCategory.getStoreSn());
            stockCategory.setStoreId(store.getId());
            stockCategory.setStoreName(store.getName());
            stockCategoryService.save(stockCategory);
        }
        return "redirect:/admin/stockCategory/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改库存分类", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        StockCategory sc = stockCategoryService.findOne(id);
        model.addAttribute("stockCategory", sc);
        return "admin/stock/stockCategory/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, StockCategory stockCategory, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            stockCategory.setNameFull(PinyinToolkit.cn2Spell(stockCategory.getName(), ""));
            stockCategory.setNameShort(PinyinToolkit.cn2FirstSpell(stockCategory.getName()));
            StockCategory sc = stockCategoryService.findOne(id);
            MyBeanUtils.copyProperties(stockCategory, sc, new String[]{"id"});

            stockCategoryService.save(sc);
        }
        return "redirect:/admin/stockCategory/list";
    }

    @AdminAuth(name="删除库存分类", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            stockCategoryService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
