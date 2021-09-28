package com.zslin.stockWx.controller;

import com.zslin.stock.service.IStockGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/5/22.
 */
@Controller
@RequestMapping(value = "weixin/stock")
public class WeixinStockNormalController {

    @Autowired
    private IStockGoodsService stockGoodsService;

    @GetMapping(value = "index")
    public String index(Model model, String storeSn, HttpServletRequest request) {
        model.addAttribute("goodsList", stockGoodsService.findAllShow(storeSn));
        return "weixin/stock/home/indexNormal";
    }
}
