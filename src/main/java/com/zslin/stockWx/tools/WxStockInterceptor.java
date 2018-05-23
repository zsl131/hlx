package com.zslin.stockWx.tools;

import com.zslin.kaoqin.model.Worker;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WxStockInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String openid = SessionTools.getOpenid(request);

        if(openid!=null && !"".equals(openid)) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());

            StockWxTools stockWxTools = (StockWxTools) factory.getBean("stockWxTools");
            Worker w = stockWxTools.getLoginWorker(openid);
            if(w==null || w.getOperator()==null || "".equals(w.getOperator())) {
                response.sendRedirect(request.getContextPath()+"/wx/stock/index");
            } else {
                StockWxTools.setWorker(request, w);
            }
        } else {
            response.sendRedirect(request.getContextPath()+"/weixin/index");
        }
        return super.preHandle(request, response, handler);
    }
}