package com.zslin.stockWx.tools;

import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/5/22.
 */
@Component("stockWxTools")
public class StockWxTools {

    //-1-2-3-4-，1-采购员；2-出库员；3-入库员；4-审核员；10-管理员
    public static final String BUYER = "1"; //采购员
    public static final String OUTER = "2"; //出库员
    public static final String INNER = "3"; //入库员
    public static final String AUDITOR = "4"; //审核员
    public static final String ADMIN = "10"; //管理员

    public boolean hasAuth(String authCode, String openid) {
        Worker w = getLoginWorker(openid);
        return hasAuth(authCode, w);
    }

    public boolean hasAuth(String authCode, Worker w) {
        String operator = w.getOperator();
        if(operator!=null && (operator.indexOf("-"+authCode+"-")>=0 || operator.indexOf("-"+ADMIN+"-")>=0)) {return true;}
        return false;
    }

    public boolean hasAuths(String openid, String... auths) {
        Worker w = getLoginWorker(openid);
        boolean res = false;
        if(w!=null && w.getOperator()!=null) {
            for(String auth: auths) {
                if(w.getOperator().indexOf("-"+auth+"-")>=0) {res = true; break;}
            }
        }
        return res;
    }

    @Autowired
    private IWorkerService workerService;

    public Worker getLoginWorker(String openid) {
        Worker w = workerService.findByOpenid(openid);
        return w;
    }

    /**
     * 获取审核员Openids
     * @return
     */
    public List<String> getAuditorOpenIds() {
        return getOpenid(AUDITOR, ADMIN);
    }

    /**
     * 获取入库员Openids
     * @return
     */
    public List<String> getInnerOpenids() {
        return getOpenid(INNER, ADMIN);
    }

    /**
     * 获取出库员Openids
     * @return
     */
    public List<String> getOuterOpenids() {
        return getOpenid(OUTER, ADMIN);
    }

    /**
     * 获取采购员Openids
     * @return
     */
    public List<String> getBuyerOpenids() {
        return getOpenid(BUYER, ADMIN);
    }

    public static void setWorker(HttpServletRequest request, Worker w) {
        request.getSession().setAttribute("loginWorker", w);
    }

    public List<String> getOpenid(String... types) {
        List<String> result = new ArrayList<>();
        for(String type : types) {
            List<String> temp = workerService.findOpenidByOperator("-"+type+"-");
            for(String openid : temp) {
                if(!result.contains(openid)) {result.add(openid);}
            }
        }
        return result;
    }
}
