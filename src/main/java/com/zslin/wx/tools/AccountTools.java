package com.zslin.wx.tools;

import com.zslin.basic.tools.NormalTools;
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

    /**
     * 判断type是否为股东或超级管理员
     * @param type 用户类型
     * @return
     */
    public static boolean isPartner(String type) {
        if(PARTNER.equalsIgnoreCase(type) || ADMIN.equalsIgnoreCase(type)) {return true;}
        return false;
    }

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

    /**
     * 生成用户人数相关的事件推送字符串
     * @return
     */
    public String buildAccountStr() {
        Integer allCount = accountService.findAllCount();
        Integer followCount = accountService.findFollowCount();
        StringBuffer sb = new StringBuffer();
        sb.append("用户总数：").append(allCount).append(" 人")
                .append("\\n").append("关注人数：").append(followCount).append(" 人")
                .append("\\n").append("今日关注：").
                append(accountService.findFollowCountByDay(NormalTools.curDate("yyyy-MM-dd"))).append(" 人")
                .append("\\n已取消：").append((allCount-followCount)).append(" 人");
        return sb.toString();
    }
}
