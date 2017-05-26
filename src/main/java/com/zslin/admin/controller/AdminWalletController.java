package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.Wallet;
import com.zslin.web.service.IWalletService;
import com.zslin.wx.dbtools.MoneyTools;
import com.zslin.wx.dbtools.ScoreTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/wallet")
@AdminAuth(name = "钱包管理", psn = "应用管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-shopping-bag")
public class AdminWalletController {

    @Autowired
    private IWalletService walletService;

    @Autowired
    private MoneyTools moneyTools;

    @Autowired
    private ScoreTools scoreTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "钱包管理", type = "1", orderNum = 1, icon = "fa fa-shopping-bag")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Wallet> datas = walletService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        return "admin/wallet/list";
    }

    @RequestMapping(value = "plus", method = RequestMethod.POST)
    @AdminAuth(name = "钱包充值", type = "1", orderNum = 1, icon = "fa fa-plus")
    public @ResponseBody String plus(String phone, String type, Float amount, String reason) {
        try {
            //type-1-积分；type-2-现金
            if("2".equals(type)) {
                moneyTools.processScoreByPhone(phone, (int) (amount * 100), reason);
            } else if("1".equals(type)) { //如果是对积分的操作，页面传过来的phone应该为openid
                scoreTools.processScoreByAmount(phone, (int) (amount*1), reason);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }
}
