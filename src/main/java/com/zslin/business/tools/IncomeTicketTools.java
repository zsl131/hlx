package com.zslin.business.tools;

import com.zslin.business.dto.IncomeTicketDto;
import com.zslin.web.model.Income;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入凭证状态检测
 *  - 检测凭证是否已正确上传
 */
public class IncomeTicketTools {

    public static List<IncomeTicketDto> checkStatus(List<Income> incomeList) {
        List<IncomeTicketDto> res = new ArrayList<>();
        for(Income income: incomeList) {
            String status = "1";
            String url = income.getTicketPath();
            if(url==null || "".equals(url.trim())) {
                status = "0";
            } else {
                String staCode = checkUrlConnection(url);
                if(!"200".equals(staCode)) {status = "-1";}
            }
            res.add(new IncomeTicketDto(income.getStoreSn(), income.getComeDay(), status));
        }
        return res;
    }

    public static String checkUrlConnection(String url) {
        // 创建http POST请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(600)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(1000)// 设置连接请求超时时间
                .setSocketTimeout(1000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpGet.setConfig(requestConfig);
        // 设置请求头
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        int statusCode = 404;
        try {
            httpclient = HttpClients.createDefault();// 创建Httpclient对象
            response = httpclient.execute(httpGet);// 执行请求
            statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            return "404";
        }
        return String.valueOf(statusCode);
    }
}
