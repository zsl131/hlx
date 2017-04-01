package com.zslin.wx.tools;

import com.zslin.web.model.WeixinConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public WeixinConfig getWxConfig() {
        return wxConfig.getConfig();
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
                WeixinConfig config = wxConfig.getConfig();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("appid", config.getAppid());
                params.put("secret", config.getSecret());
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

    public String saveMedia(String mediaId, String path) {
        String res = "";
        try {
            String urlStr = "https://api.weixin.qq.com/cgi-bin/media/get?access_token="+accessTokenTools.getAccessToken()+"&media_id="+mediaId;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.connect();
//            conn.getInputStream();
            Map<String, List<String>> map = conn.getHeaderFields();
            String fileNameHead = map.get("Content-disposition").get(0);
            String fileType = getFileType(fileNameHead);
            saveFile(conn.getInputStream(), path+fileType);
            res = path + fileType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String getFileType(String str) {
        str = str.replace("\"", "");
        return str.substring(str.lastIndexOf("."));
    }

    private void saveFile(InputStream is, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            byte [] b = new byte[1024];
            int len = 0;
            while((len=is.read(b))!=-1) {
                fos.write(b, 0, len);
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
