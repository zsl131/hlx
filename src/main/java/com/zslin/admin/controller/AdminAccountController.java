package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.DatasTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 22:15.
 */
@Controller
@RequestMapping(value = "admin/account")
@AdminAuth(name = "微信用户", psn = "应用管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-user-circle")
public class AdminAccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private DatasTools datasTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "微信用户", orderNum = 1, type = "1", icon = "fa fa-user-circle")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Account> datas = accountService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/account/list";
    }

    @AdminAuth(name="同步微信用户", orderNum=4, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method= RequestMethod.POST)
    public @ResponseBody
    String update(@PathVariable Integer id) {
        try {
            datasTools.updateAccount(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @PostMapping(value = "updateType")
    public @ResponseBody String updateType(Integer id, String type) {
        try {
            accountService.updateType(id, type);
        } catch (Exception e) {
            return "0";
        }
        return "1";
    }
}
