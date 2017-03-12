package com.zslin.client.tools;

import com.alibaba.fastjson.JSON;
import com.zslin.client.dto.JsonDto;
import com.zslin.client.model.ClientConfig;
import com.zslin.kaoqin.model.Worker;
import com.zslin.web.model.Price;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 11:29.
 */
public class ClientJsonTools {

    /**
     * 构建能发送的Json数据
     * @param dataJson
     * @return
     */
    public static String buildDataJson(String dataJson) {
        StringBuffer sb = new StringBuffer();
        sb.append("{status:1,info:\"ok\",data:[");
        sb.append(dataJson);
        sb.append("]}");
        return sb.toString();
    }

    public static String buildConfig(ClientConfig cc) {
        JsonDto jd = new JsonDto("config", "update", cc.getId(), cc);
        return JSON.toJSONString(jd);
    }

    public static String buildWorker(String action, Worker w) {
        JsonDto jd = new JsonDto("worker", action, w.getId(), w);
        return JSON.toJSONString(jd);
    }

    public static String buildPrice(Price p) {
        JsonDto jd = new JsonDto("price", "update", p.getId(), p);
        return JSON.toJSONString(jd);
    }
}
