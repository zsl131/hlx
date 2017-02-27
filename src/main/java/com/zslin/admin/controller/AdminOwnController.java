package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Own;
import com.zslin.web.service.IOwnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:41.
 */
@Controller
@RequestMapping(value = "admin/own")
@AdminAuth(name = "礼品管理", psn = "应用管理", orderNum = 10, porderNum = 1, pentity = 0, icon = "fa fa-cubes")
public class AdminOwnController {

    @Autowired
    private IOwnService ownService;

    @GetMapping(value = "list")
    @AdminAuth(name = "礼品管理", type = "1", orderNum = 1, icon = "fa fa-comment")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Own> datas = ownService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/own/list";
    }
}