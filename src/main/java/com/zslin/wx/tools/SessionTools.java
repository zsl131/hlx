package com.zslin.wx.tools;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/7 22:27.
 */
public class SessionTools {

    private static final String OPENID = "openid";

    /** 获取存在Session中的openid */
    public static String getOpenid(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(OPENID);
    }

    /** 将openid存入session中 */
    public static void setOpenid(HttpServletRequest request, String openid) {
        request.getSession().setAttribute(OPENID, openid);
    }
}
