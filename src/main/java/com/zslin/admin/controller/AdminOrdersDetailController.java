package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.BuffetOrderDetail;
import com.zslin.web.service.IBuffetOrderDetailService;
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
@RequestMapping(value = "admin/ordersDetail")
@AdminAuth(name = "订单详情管理", psn = "订单管理", orderNum = 10, porderNum = 1, pentity = 0, icon = "fa fa-list")
public class AdminOrdersDetailController {

    @Autowired
    private IBuffetOrderDetailService buffetOrderDetailService;

    /** 订单列表 */
    @GetMapping(value = "list")
    @AdminAuth(name = "订单详情管理", orderNum = 1, type = "1", icon = "fa fa-list")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<BuffetOrderDetail> datas = buffetOrderDetailService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createLong_d")));
        model.addAttribute("datas", datas);
        return "admin/ordersDetail/list";
    }
}
