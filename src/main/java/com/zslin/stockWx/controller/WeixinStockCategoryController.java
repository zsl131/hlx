package com.zslin.stockWx.controller;

import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.model.StockCategory;
import com.zslin.stock.service.IStockCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/5/22.
 */
@Controller
@RequestMapping(value = "wx/stock/stockCategory")
public class WeixinStockCategoryController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    public String list(Model model, String storeSn, HttpServletRequest request) {
        List<StockCategory> list1 = stockCategoryService.listByLocationTypeAndStoreSn("1", storeSn);
        List<StockCategory> list2 = stockCategoryService.listByLocationTypeAndStoreSn("2", storeSn);
        List<StockCategory> list3 = stockCategoryService.listByLocationTypeAndStoreSn("3", storeSn);
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        model.addAttribute("list3", list3);
        return "weixin/stock/stockCategory/list";
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public @ResponseBody String add(String locationType, String storeSn, String categoryName) {
        StockCategory stockCategory = new StockCategory();
        stockCategory.setLocationType(locationType);
        stockCategory.setName(categoryName);
        stockCategory.setNameFull(PinyinToolkit.cn2Spell(categoryName, ""));
        stockCategory.setNameShort(PinyinToolkit.cn2FirstSpell(categoryName));
        Store store = storeDao.findBySn(storeSn);
        stockCategory.setStoreName(store.getName());
        stockCategory.setStoreId(store.getId());
        stockCategory.setStoreSn(storeSn);
        stockCategoryService.save(stockCategory);
        return "1";
    }

    @RequestMapping(value="update", method=RequestMethod.POST)
    public @ResponseBody String update(Integer id, String categoryName) {
        StockCategory stockCategory = stockCategoryService.findOne(id);
        stockCategory.setNameFull(PinyinToolkit.cn2Spell(categoryName, ""));
        stockCategory.setNameShort(PinyinToolkit.cn2FirstSpell(categoryName));
        stockCategory.setName(categoryName);
        stockCategoryService.save(stockCategory);
        return "1";
    }

    @RequestMapping(value="delete", method= RequestMethod.POST)
    public @ResponseBody
    String delete(Integer id) {
        try {
            stockCategoryService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
