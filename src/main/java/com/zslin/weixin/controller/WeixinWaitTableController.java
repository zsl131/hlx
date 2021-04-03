package com.zslin.weixin.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.multi.dao.ITableCategoryDao;
import com.zslin.multi.dao.IWaitTableDao;
import com.zslin.multi.model.WaitTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 排队处理
 */
@Controller
@RequestMapping(value = "weixin/waitTable")
public class WeixinWaitTableController {

    @Autowired
    private IWaitTableDao waitTableDao;

    @Autowired
    private ITableCategoryDao tableCategoryDao;

    @GetMapping(value = "index")
    public String index(Model model, String storeSn, String flag, HttpServletRequest request) {
        String days = NormalTools.curDate("yyyyMMdd");
        storeSn = (storeSn==null || "".equals(storeSn.trim()))? ClientFileTools.QWZW_SN:storeSn;
        List<WaitTable> waitList ;
        if(flag==null || "".equals(flag.trim())) {waitList = waitTableDao.findByStoreSnAndCreateDay(storeSn, days);}
        else {waitList = waitTableDao.findByStoreSnAndCreateDayAndCateFlag(storeSn, days, flag);}
        model.addAttribute("waitList", waitList);
        model.addAttribute("categoryList", tableCategoryDao.findByStoreSn(storeSn));
        return "weixin/waitTable/index";
    }

//    public
}
