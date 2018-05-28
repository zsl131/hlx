package com.zslin.stockWx.controller;

import com.zslin.kaoqin.model.Worker;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stockWx.tools.StockWxTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/5/22.
 */
@Controller
@RequestMapping(value = "wx/stock")
public class WeixinStockController {

    @Autowired
    private StockWxTools stockWxTools;

    @Autowired
    private IStockGoodsService stockGoodsService;

    /** 列表库存预警信息 */
    @GetMapping(value = "listWarn")
    public String listWarn(Model model, HttpServletRequest request) {
        List<StockGoods> list = stockGoodsService.findAllWarn();
        model.addAttribute("list", list);
        return "weixin/stock/home/listWarn";
    }

    @GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Worker w = stockWxTools.getLoginWorker(openid);
        model.addAttribute("loginWorker", w);
        return "weixin/stock/home/index";
    }
}
