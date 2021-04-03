package com.zslin.multi.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.dao.ITableCategoryDao;
import com.zslin.multi.dao.IWaitTableDao;
import com.zslin.multi.model.Store;
import com.zslin.multi.model.TableCategory;
import com.zslin.multi.model.WaitTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:11.
 */
@Controller
@RequestMapping(value = "admin/waitTable")
@AdminAuth(name = "排队管理", psn = "排队管理", orderNum = 6, porderNum = 1, pentity = 0, icon = "fa fa-tasks")
public class AdminWaitTableController {

    @Autowired
    private ITableCategoryDao tableCategoryDao;

    @Autowired
    private IStoreDao storeDao;

    @Autowired
    private IWaitTableDao waitTableDao;

    @GetMapping(value = "list")
    @AdminAuth(name = "排队管理", orderNum = 1, type = "1", icon = "fa fa-tasks")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<WaitTable> datas = waitTableDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        model.addAttribute("categoryList", tableCategoryDao.findAll());
        return "admin/waitTable/list";
    }
}
