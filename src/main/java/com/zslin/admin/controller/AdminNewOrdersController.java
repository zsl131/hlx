package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.BuffetOrder;
import com.zslin.web.service.IBuffetOrderDetailService;
import com.zslin.web.service.IBuffetOrderService;
import com.zslin.web.service.ICommodityService;
import com.zslin.web.service.IRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/7 0:48.
 */
@Controller
@RequestMapping(value = "admin/newOrders")
@AdminAuth(name = "订单管理", psn = "订单管理", orderNum = 10, porderNum = 1, pentity = 0, icon = "fa fa-list")
public class AdminNewOrdersController {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IRulesService rulesService;

    /** 订单列表 */
    @GetMapping(value = "list")
    @AdminAuth(name = "订单管理", orderNum = 1, type = "1", icon = "fa fa-list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<BuffetOrder> datas = buffetOrderService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createLong_d")));
        model.addAttribute("datas", datas);
        return "admin/newOrders/list";
    }

    @GetMapping(value = "cal")
    @AdminAuth(name = "收银对账", orderNum = 2, type = "1", icon = "fa fa-cny")
    public String cal(Model model, String day, HttpServletRequest request) {
        if(day==null) {
            day = NormalTools.curDate("yyyy-MM-dd"); //默认为当天
        } else {
            day = day.replace("eq-", "");
        }
        Integer halfAm = buffetOrderDetailService.queryCount(day, "66666"); //午餐半票人数
        Integer fullAm = buffetOrderDetailService.queryCount(day, "88888"); //午餐全票人数
        Integer halfPm = buffetOrderDetailService.queryCount(day, "77777"); //晚餐半票人数
        Integer fullPm = buffetOrderDetailService.queryCount(day, "99999"); //晚餐全票人数


        model.addAttribute("halfAm", halfAm);
        model.addAttribute("fullAm", fullAm);
        model.addAttribute("halfPm", halfPm);
        model.addAttribute("fullPm", fullPm);
        return "admin/newOrders/cal";
    }
}
