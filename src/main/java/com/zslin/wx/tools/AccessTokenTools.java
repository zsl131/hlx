package com.zslin.wx.tools;

import com.zslin.cache.CacheTools;
import com.zslin.web.model.WeixinConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 11:05.
 */
@Component
public class AccessTokenTools {

    private static final String NAME = "wx-access-token";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String TICKET_NAME = "wx-jsapi-ticket";
    private static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";

    @Autowired
    private CacheTools cacheTools;
    @Autowired
    private WxConfig wxConfig;

    public String getAccessToken() {
        String token = (String) cacheTools.getKey(NAME);
        if(token==null || "".equals(token)) {
            token = getNewAccessToken();
            cacheTools.putKey(NAME, token, 7000);
        }
        return token;
    }

    private String getNewAccessToken() {
        WeixinConfig config = wxConfig.getConfig();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("grant_type", "client_credential");
        params.put("appid", config.getAppid());
        params.put("secret", config.getSecret());
        String result = InternetTools.doGet(TOKEN_URL, params);
        return JsonTools.getJsonParam(result, "access_token");
    }

    public String getJsTicket() {
        String ticket = (String) cacheTools.getKey(TICKET_NAME);
        if(ticket==null || "".equals(ticket)) {
            ticket = getNewTicket();
            cacheTools.putKey(TICKET_NAME, ticket, 7000);
        }
        return ticket;
    }

    private String getNewTicket() {
        String url = TICKET_URL+getAccessToken();
        String result = InternetTools.doGet(url, null);
        return JsonTools.getJsonParam(result, "ticket");
    }
}
