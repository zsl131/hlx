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
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IStockCategoryService;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stock.tools.GoodsNoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/5/21.
 * 库存管理-物品管理
 */
@Controller
@RequestMapping(value = "admin/stockGoods")
@AdminAuth(name = "物品管理", psn = "库存管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-gift")
public class AdminStockGoodsController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "库存物品管理", orderNum = 1, type = "1", icon = "fa fa-gift")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<StockGoods> datas = stockGoodsService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id")));
        model.addAttribute("datas", datas);
        model.addAttribute("cateList", stockCategoryService.findAll()); //获取所有分类数据
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/stock/stockGoods/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加库存物品", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("stockGoods", new StockGoods());
        model.addAttribute("cateList", stockCategoryService.findAll()); //获取所有分类数据
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/stock/stockGoods/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, StockGoods stockGoods, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            stockGoods.setNameFull(PinyinToolkit.cn2Spell(stockGoods.getName(), ""));
            stockGoods.setNameShort(PinyinToolkit.cn2FirstSpell(stockGoods.getName()));
            Integer orderNo = goodsNoTools.generateOrderNo(stockGoods.getStoreSn());
            stockGoods.setOrderNo(orderNo);
            stockGoods.setHasWarn("1");
            try {
                Store store = storeDao.findBySn(stockGoods.getStoreSn());
                stockGoods.setStoreId(store.getId());
                stockGoods.setStoreName(store.getName());
            } catch (Exception e) {
            }
            stockGoods.setNo(goodsNoTools.buildNo(stockGoods.getLocationType(), orderNo));
            stockGoods.setAmount(0); //初次添加数量都为0 ，如有数量应从入库添加
            stockGoodsService.save(stockGoods);
        }
        return "redirect:/admin/stockGoods/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改库存物品", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        StockGoods sg = stockGoodsService.findOne(id);
        model.addAttribute("stockGoods", sg);
        return "admin/stock/stockGoods/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, StockGoods stockGoods, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            stockGoods.setNameFull(PinyinToolkit.cn2Spell(stockGoods.getName(), ""));
            stockGoods.setNameShort(PinyinToolkit.cn2FirstSpell(stockGoods.getName()));
            StockGoods sg = stockGoodsService.findOne(id);
            MyBeanUtils.copyProperties(stockGoods, sg, new String[]{"id", "amount", "orderNo", "no","cateId", "cateName", "locationType", "hasWarn"});

            stockGoodsService.save(sg);
        }
        return "redirect:/admin/stockGoods/list";
    }

    @AdminAuth(name="删除库存物品", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            stockGoodsService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="修改库存物品状态", orderNum=4, icon = "fa fa-pencil")
    @RequestMapping(value="updateStatus/{id}/{status}", method=RequestMethod.POST)
    public @ResponseBody
    String updateStatus(@PathVariable Integer id, @PathVariable String status) {
        try {
            stockGoodsService.updateStatus(status, id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
