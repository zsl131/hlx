package com.zslin.client.tools;

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
}
