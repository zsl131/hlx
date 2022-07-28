package com.zslin.wx.controller;

import com.zslin.finance.dao.IFinanceCategoryDao;
import com.zslin.finance.model.FinanceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "weixin/finance/category")
public class WeixinFinanceCategoryController {

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @PostMapping(value = "queryCategory")
    @ResponseBody
    public List<FinanceCategory> queryCategory(String storeSn) {
        return financeCategoryDao.queryCategoryByStoreSn(storeSn);
    }
}
