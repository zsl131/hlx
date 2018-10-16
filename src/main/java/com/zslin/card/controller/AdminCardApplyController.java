package com.zslin.card.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.model.CardApply;
import com.zslin.card.service.IApplyReasonService;
import com.zslin.card.service.ICardApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "admin/cardApply")
@AdminAuth(name = "代金券申请管理", psn = "卡券管理", orderNum = 9, porderNum = 10, pentity = 0, icon = "fa fa-magnet")
public class AdminCardApplyController {

    @Autowired
    private ICardApplyService cardApplyService;

    @Autowired
    private IApplyReasonService applyReasonService;

    @GetMapping(value = "list")
    @AdminAuth(name = "代金券申请管理", type = "1", orderNum = 1, icon = "fa fa-magnet")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<CardApply> datas = cardApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("reasonList", applyReasonService.findAll());
        return "admin/cardApply/list";
    }
}
