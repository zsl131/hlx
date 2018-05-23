package com.zslin.stockWx.tools;

import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/5/22.
 */
@Component("stockWxTools")
public class StockWxTools {

    @Autowired
    private IWorkerService workerService;

    public Worker getLoginWorker(String openid) {
        Worker w = workerService.findByOpenid(openid);
        return w;
    }

    public static void setWorker(HttpServletRequest request, Worker w) {
        request.getSession().setAttribute("loginWorker", w);
    }
}
