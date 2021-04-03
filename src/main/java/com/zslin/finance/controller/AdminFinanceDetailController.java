package com.zslin.finance.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.finance.dao.IFinanceCategoryDao;
import com.zslin.finance.dao.IFinanceDetailDao;
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
@RequestMapping(value="admin/financeDetail")
@AdminAuth(name="财务报账管理", orderNum=10, psn="财务管理", pentity=0, porderNum=20)
public class AdminFinanceDetailController {

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @Autowired
    private IStoreDao storeDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "财务报账管理", orderNum = 1, type = "1", icon = "fa fa-cny")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<FinanceDetail> datas = financeDetailDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        model.addAttribute("categoryList", financeCategoryDao.findAll());
        model.addAttribute("storeList", storeDao.findByStatus("1"));
        return "admin/finance/financeDetail/list";
    }
}
