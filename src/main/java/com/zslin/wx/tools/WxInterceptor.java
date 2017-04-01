package com.zslin.wx.tools;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class WxInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String openid = SessionTools.getOpenid(request);

        String realpath = fullPath(request); //获取全路径
        if(openid==null || "".equals(openid)) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            ExchangeTools exchangeTools = (ExchangeTools) factory.getBean("exchangeTools");
            String code = request.getParameter("code");
            String state = request.getParameter("state");  //正确值：hztOauth2，参考授权跳转地址
            if(code!=null && !"".equals(code) && state!=null && state.equals("hztOauth2")) { //如果code存在
//                openid = WeixinConstant.getUserOpenId(request, code); //获取openid
                openid = exchangeTools.getUserOpenId(code); //获取openid

                if(openid!=null && !"".equals(openid.trim()) && !"null".equalsIgnoreCase(openid)) {
                    SessionTools.setOpenid(request, openid);
                } else {
                    System.out.println("=====获取openid异常："+realpath);
                }
            } else { //如果code不存在，则重新获取

                String fullPath = URLEncoder.encode(realpath, "UTF-8");
                String appId = exchangeTools.getWxConfig().getAppid();
                String authPath = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=hztOauth2#wechat_redirect";
                authPath = authPath.replace("APPID", appId).replace("REDIRECT_URI", fullPath);
                response.sendRedirect(authPath);
                return false;
            }
        }
//        System.out.println("==========currentOpenid:"+openid);
//        request.removeAttribute("code"); //移除这两个参数，以免session过期时还使用这个参数获取openid
        request.removeAttribute("state"); //移除这两个参数，以免session过期时还使用这个参数获取openid
        return super.preHandle(request, response, handler);
    }

    /** 获取全路径 */
    private String fullPath(HttpServletRequest request) {
        String param = request.getQueryString()==null?"":request.getQueryString();
        if(param==null || "".equals(param) || param.indexOf("openid=")<0) {
            String openid = (String) request.getSession().getAttribute("openid");
            if(openid!=null && !"".equals(openid)) {
                param = "openid="+openid+"&"+param;
            }
        }
        String res = request.getRequestURL().toString()+"?" + param;
        return res;
    }
}