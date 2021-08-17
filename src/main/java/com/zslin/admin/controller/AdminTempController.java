package com.zslin.admin.controller;

import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.ExchangeTools;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/10/5.
 */
@RestController
@RequestMapping(value = "admin/temp")
public class AdminTempController {

    @Autowired
    private ExchangeTools exchangeTools;

    @Autowired
    private IAccountService accountService;

    @GetMapping(value = "listWxUsers")
    public String listWxUsers(HttpServletRequest request) {
        exchangeTools.listWxUsers(null);
        return "处理完成";
    }

    /** 更新unionid */
    @GetMapping(value = "processUnionid")
    public String processUnionid(HttpServletRequest request) {
        List<String> openidList = accountService.findNoUnionid();
        for(String openid: openidList) {
            try {
                JSONObject jsonObj = exchangeTools.getUserInfo(openid);
                String unionid = jsonObj.getString("unionid");
                accountService.updateUnionid(unionid, openid);
            } catch (Exception e) {
            }
        }
        return "处理完成";
    }
}
