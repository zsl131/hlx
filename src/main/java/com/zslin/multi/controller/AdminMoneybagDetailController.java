package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IMoneybagDao;
import com.zslin.multi.dao.IMoneybagDetailDao;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.MoneybagDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/moneybagDetail")
@AdminAuth(name="会员详情管理", orderNum=10, psn="多店管理", pentity=0, porderNum=20)
public class AdminMoneybagDetailController {

    @Autowired
    private IMoneybagDao moneybagDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IMoneybagDetailDao moneybagDetailDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "会员详情列表", orderNum = 1, type = "1", icon = "fa fa-money")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<MoneybagDetail> datas = moneybagDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findAll());
        return "admin/multi/moneybagDetail/list";
    }
}
