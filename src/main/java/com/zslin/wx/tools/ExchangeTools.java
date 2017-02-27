package com.zslin.wx.tools;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 23:38.
 * 与微信的数据交换
 */
@Component("exchangeTools")
public class ExchangeTools {

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private WxConfig wxConfig;

    public WxConfig getWxConfig() {
        return wxConfig;
    }

    public JSONObject getUserInfo(String openid) {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("access_token", accessTokenTools.getAccessToken());
            params.put("openid", openid);
            params.put("lang", "zh_CN");

            String result = InternetTools.doGet("https://api.weixin.qq.com/cgi-bin/user/info", params);

            JSONObject jsonObj = new JSONObject(result);
            return jsonObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 获取openid */
    public String getUserOpenId(String code) {
        try {
            String openid = null;
            if(code!=null && !"".equals(code.trim())) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("appid", wxConfig.getAppid());
                params.put("secret", wxConfig.getSecret());
                params.put("code", code);
                params.put("grant_type", "authorization_code");
                String result = InternetTools.doGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
//				System.out.println(code+"获取openid="+result);
                if(result==null) { //如果由于网络等原因，result为空时，再获取一次
                    result = InternetTools.doGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
                }
                String res = JsonTools.getJsonParam(result, "openid");
                if(res==null || "".equals(res)) {
                    System.out.println("===未获取openid==="+result+"===code:"+code);
                }
                return res;
            } return "";
        } catch (Exception e) {
            return "";
        }
    }
}
