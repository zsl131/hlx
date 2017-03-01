package com.zslin.wx.tools;

import com.zslin.web.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 11:07.
 */
@Component
public class AccountTools {

    @Autowired
    private IAccountService accountService;

    public static final String CUSTOMER = "0"; //顾客
    public static final String WORKER = "1"; //员工
    public static final String MANAGER = "2"; //部门经理
    public static final String PARTNER = "5"; //股东
    public static final String ADMIN = "10"; //超级管理员

    public List<String> getOpenid(String... types) {
        List<String> result = new ArrayList<>();
        for(String type : types) {
            List<String> temp = accountService.findOpenid(type);
            for(String openid : temp) {
                if(!result.contains(openid)) {result.add(openid);}
            }
        }
        return result;
    }
}
