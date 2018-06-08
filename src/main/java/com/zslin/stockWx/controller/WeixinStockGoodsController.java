package com.zslin.stockWx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.stock.model.StockCategory;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IStockCategoryService;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stock.tools.GoodsNoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = "wx/stock/stockGoods")
public class WeixinStockGoodsController {

    @Autowired
    private IStockCategoryService stockCategoryService;

    @Autowired
    private IStockGoodsService stockGoodsService;

    @Autowired
    private GoodsNoTools goodsNoTools;

    @GetMapping(value = "list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        try {
            String cateIdStr = request.getParameter("filter_cateId");
            if(cateIdStr!=null && !"".equals(cateIdStr)) {
                Integer cateId = Integer.parseInt(cateIdStr.split("-")[1]);
                model.addAttribute("category", stockCategoryService.findOne(cateId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<StockGoods> datas = stockGoodsService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        List<StockCategory> list1 = stockCategoryService.listByLocationType("1");
        List<StockCategory> list2 = stockCategoryService.listByLocationType("2");
        List<StockCategory> list3 = stockCategoryService.listByLocationType("3");
        model.addAttribute("list1", list1);
        model.addAttribute("list2", list2);
        model.addAttribute("list3", list3);
        model.addAttribute("datas", datas);
        return "weixin/stock/stockGoods/list";
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public @ResponseBody String add(Integer cateId, String name, String unit, String remark, Integer warnAmount, String status) {
        StockCategory sc = stockCategoryService.findOne(cateId);
        StockGoods sg = new StockGoods();
        sg.setNameFull(PinyinToolkit.cn2Spell(name, ""));
        sg.setNameShort(PinyinToolkit.cn2FirstSpell(name));
        sg.setName(name);
        sg.setUnit(unit);
        sg.setRemark(remark);
        sg.setWarnAmount(warnAmount);
        sg.setStatus(status);
        sg.setCateId(cateId);
        sg.setHasWarn("1");
        sg.setAmount(0); //初次添加数量都为0 ，如有数量应从入库添加
        sg.setCateName(sc.getName());
        sg.setLocationType(sc.getLocationType());
        Integer orderNo = goodsNoTools.generateOrderNo();
        sg.setOrderNo(orderNo);
        sg.setNo(goodsNoTools.buildNo(sg.getLocationType(), orderNo));

        stockGoodsService.save(sg);
        return "1";
    }

    @RequestMapping(value="update", method=RequestMethod.POST)
    public @ResponseBody String update(Integer id, String name, String unit, String remark, Integer warnAmount, String status) {
        StockGoods sg = stockGoodsService.findOne(id);
        sg.setNameFull(PinyinToolkit.cn2Spell(name, ""));
        sg.setNameShort(PinyinToolkit.cn2FirstSpell(name));
        sg.setName(name);
        sg.setUnit(unit);
        sg.setRemark(remark);
        sg.setWarnAmount(warnAmount);
        sg.setStatus(status);
        stockGoodsService.save(sg);
        return "1";
    }

    @RequestMapping(value="delete", method= RequestMethod.POST)
    public @ResponseBody
    String delete(Integer id) {
        try {
            stockGoodsService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
