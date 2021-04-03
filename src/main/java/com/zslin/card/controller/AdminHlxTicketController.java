package com.zslin.card.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.weixin.model.HlxTicket;
import com.zslin.weixin.service.IHlxTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "admin/hlxTicket")
@AdminAuth(name = "电子卡券管理", psn = "卡券管理", orderNum = 9, porderNum = 10, pentity = 0, icon = "fa fa-credit-card")
public class AdminHlxTicketController {

    @Autowired
    private IHlxTicketService hlxTicketService;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "电子卡券管理", type = "1", orderNum = 1, icon = "fa fa-credit-card")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<HlxTicket> datas = hlxTicketService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/hlxTicket/list";
    }

    @AdminAuth(name="设置卡券状态", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="abolish/{id}", method= RequestMethod.POST)
    public @ResponseBody
    String abolish(@PathVariable Integer id, String status) {
        try {
            hlxTicketService.updateStatus(status, id); //作废
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
