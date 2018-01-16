package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.model.Restday;
import com.zslin.client.service.IRestdayService;
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
@RequestMapping(value = "admin/restday")
@AdminAuth(name = "工作日管理", psn = "应用管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-clock-o")
public class AdminRestdayController {

    @Autowired
    private IRestdayService restdayService;

    @GetMapping(value = "list")
    @AdminAuth(name = "工作日管理", type = "1", orderNum = 1, icon = "fa fa-shopping-bag")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Restday> datas = restdayService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/restday/list";
    }
}
