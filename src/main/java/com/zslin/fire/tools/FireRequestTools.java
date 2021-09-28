package com.zslin.fire.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.fire.model.FireConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FireRequestTools {

    @Autowired
    private FireConfig fireConfig;

    private final String VERSION_1 = "1.0";

    private final String VERSION_2 = "2.0";

    private String requestUrl ;

    public FireRequestTools setUrl(String url) {
        this.requestUrl = url;
        return this;
    }

    public void balance() {
        Map<String, Object> form = new HashMap<>();
        form.put("v", VERSION_2);
        form.put("method", "com.dfire.open.menu.balance.query");
//        form.put("entityId", "00540580");

        Map<String, Object> data = new HashMap<>();
        data.put("entityId", fireConfig.getEntityId());
        form.put("query", JSON.toJSONString(data));

        doRequest(form);
    }

    public void paymentFlow() {
        Map<String, Object> form = new HashMap<>();
        form.put("v", VERSION_2);
        form.put("method", "com.dfire.open.report.daily.statistics");
//        form.put("entityId", "00540580");
        form.put("startDate", "20210920");
        form.put("endDate", "20210920");
        doRequest(form);
    }

    private void doRequest(Map<String, Object> form) {
        CloseableHttpResponse response = null;
        try {
            //如果不设置URL，则默认URL为final
            String url = this.requestUrl==null?fireConfig.getUrlFinal():this.requestUrl;
//            String url = "http://gateway.visa.2dfire-daily.com";
//            final String APP_KEY = "d693789f2be71d7a30581778edd1166f";
//            final String APP_SECRET = "cad7c29c60ca66e55877936c94f67a7a";

            String APP_KEY = fireConfig.getAppKey();
            String APP_SECRET = fireConfig.getAppSecret();

            //设置接口参数
//            Map< String, Object> form = new HashMap<>();
//            form.put("method", "com.dfire.open.promotion.verify");
//            form.put("method", method);
            form.put("app_key", APP_KEY);
//            form.put("v", version);
//            form.put("v", "1.0");
            form.put("lang", "zh_CN");
            form.put("timestamp", String.valueOf(System.currentTimeMillis()));
            form.put("entityId", fireConfig.getEntityId());
            /*form.put("entityId","99934033");
            List< String> orderIds  = new ArrayList<>();
            orderIds.add("99934033620a7f75016218298d6b0039");
            orderIds.add("99934033620a7f75016218a4f5640049");

            form.put("orderIds", JSON.toJSONString(orderIds));*/

            form.put("env","publish"); //日常(daily)，预发(pre)，正式(publish)
            //对请求参数列表进行签名
            String sign = RopUtils.sign(form, APP_SECRET);
            form.put("sign", sign);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000)
                    .setConnectionRequestTimeout(3000).setSocketTimeout(3000).build();

            //http请求第三方
            String dynamicParameter = "";
            for (Map.Entry< String, Object> entry : form.entrySet()) {
                dynamicParameter+=entry.getKey()+"="+ URLEncoder.encode(entry.getValue().toString(),"utf-8")+"&";
            }
            String postUrl = url +"/?"+dynamicParameter;

            CloseableHttpClient httpclient;
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(postUrl);// 创建httpPost
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                System.out.println(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
