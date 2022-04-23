package com.zslin.business.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.business.dao.IBusinessLookRecordDao;
import com.zslin.business.model.BusinessLookRecord;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.multi.dao.IStoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "admin/businessLookRecord")
@AdminAuth(name="经营浏览记录", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminBusinessLookRecordController {

    @Autowired
    private IBusinessLookRecordDao businessLookRecordDao;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "经营浏览记录管理", orderNum = 1, type = "1", icon = "fa fa-eye")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<BusinessLookRecord> datas = businessLookRecordDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/business/lookRecord/list";
    }
}
