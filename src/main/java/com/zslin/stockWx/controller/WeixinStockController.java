package com.zslin.stockWx.controller;

import com.zslin.kaoqin.model.Worker;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.stock.model.StockGoods;
import com.zslin.stock.model.StockUser;
import com.zslin.stock.service.IStockGoodsService;
import com.zslin.stock.service.IStockUserService;
import com.zslin.stockWx.tools.StockWxTools;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
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

    @Autowired
    private IStockUserService stockUserService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IStoreDao storeDao;

    /** 列表库存预警信息 */
    @GetMapping(value = "listWarn")
    public String listWarn(Model model, String storeSn, HttpServletRequest request) {
        List<StockGoods> list = stockGoodsService.findAllWarn(storeSn);
        model.addAttribute("list", list);
        return "weixin/stock/home/listWarn";
    }

    /*@GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Worker w = stockWxTools.getLoginWorker(openid);
        model.addAttribute("loginWorker", w);
        if(w==null || w.getOperator()==null || "".equals(w.getOperator())) {
            model.addAttribute("goodsList", stockGoodsService.findAllShow("hlx"));
            return "weixin/stock/home/indexNormal";
        } else {

            return "weixin/stock/home/index";
        }
    }*/

    @GetMapping(value = "index")
    public String index(Model model, String storeSn, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        try {
            Account account = accountService.findByOpenid(openid);
            StockUser stockUser = stockUserService.findByPhone(account.getPhone());
            List<Store> storeList = storeDao.findByIds(stockUserService.listStoreIds(stockUser.getId()));
            Store store ;
            if(storeSn!=null && !"".equals(storeSn)) {store = storeDao.findBySn(storeSn); if(store==null) {store = storeList.get(0);}}
            else {store = storeList.get(0);}

            model.addAttribute("storeList", storeList);
            model.addAttribute("storeUser", stockUser);
            model.addAttribute("store", store);
            return "weixin/stock/home/index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("goodsList", stockGoodsService.findAllShow("hlx"));
            return "weixin/stock/home/indexNormal";
        }
    }
}
