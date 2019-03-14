package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.DiscountDay;
import com.zslin.web.service.IDiscountDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/1/13.
 */
@Controller
@RequestMapping(value = "admin/discountDay")
@AdminAuth(name = "折扣日管理", psn = "折扣管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-clock-o")
public class AdminDiscountDayController {

    @Autowired
    private IDiscountDayService discountDayService;

    @GetMapping(value = "list")
    @AdminAuth(name = "折扣日管理", type = "1", orderNum = 1, icon = "fa fa-shopping-bag")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DiscountDay> datas = discountDayService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/discountDay/list";
    }
}
