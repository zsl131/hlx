package com.zslin.wx.tools;

import com.zslin.basic.tools.ConfigTools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/21 17:02.
 * 生成微信二维码工具类
 * 生成的二维码可能有多种作用，所以需要使用各种标识来识别
 */
@Component
public class QrTools {

    //用户标识
    public static final String USER_TYPE = "user_";

    //券标识
    public static final String TICKET_TYPE = "ticket_";

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private ConfigTools configTools;

    /**
     * 生成带参二维码
     * @param value 值
     * @param forever 是否永久
     * @return
     */
    public String genTicketQr(String value, boolean forever) {
        value = TICKET_TYPE+value;
//        System.out.println("-----------QrTools-->value:: "+value);
        String picPath = genQr(value, "ticket", forever);
        PictureTools.markImageByUrl("http://img.zslin.com/wq.jpg", configTools.getFilePath()+picPath, configTools.getFilePath()+picPath);
        return picPath;
    }

    /** 生成券二维码 */
    public String genTicketQr(String value) {
        return genTicketQr(value, true);
        /*value = TICKET_TYPE+value;
        String picPath = genQr(value, "ticket");
        PictureTools.markImageByUrl("http://img.zslin.com/hlx.jpg", configTools.getFilePath()+picPath, configTools.getFilePath()+picPath);
        return picPath;*/
    }

    public String genUserQr(String value, String headimg) {
        value = USER_TYPE+value;
        String picPath = genQr(value, "user", true);
        PictureTools.markImageByUrl(headimg, configTools.getFilePath()+picPath, configTools.getFilePath()+picPath);
        return picPath;
    }

    public String getQrTicket(String value, boolean forever) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessTokenTools.getAccessToken();
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", forever?buildForOverParam(value):buildTempParam(value));
//        System.out.println(jsonObj.toString());
        String ticket = JsonTools.getJsonParam(jsonObj.toString(), "ticket");
        String qrUrl = JsonTools.getJsonParam(jsonObj.toString(), "url");
//        System.out.println(ticket);
//        System.out.println(qrUrl);

        return ticket;
    }

    private String genQr(String value, String path, boolean forever) {
        String res = "/wxqr/"+path+"/"+value+".jpg";
        try {
            String ticket = getQrTicket(value, forever);
            String urlString = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 设置请求超时为5s
            con.setConnectTimeout(15 * 1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(configTools.getFilePath("/wxqr/"+path) + value+".jpg");
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String buildForOverParam(String value) {
        //{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
        //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
        StringBuffer sb = new StringBuffer();
        sb.append("{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"").
                append(value).append("\"}}}");
        return sb.toString();
    }

    /** 临时二维码 */
    private String buildTempParam(String value) {
        //{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene": {"scene_id": 123}}}
        //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "123"}}}
        //expire_seconds: 604800 ,七天
        StringBuffer sb = new StringBuffer();
        sb.append("{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"").
                append(value).append("\"}}}");

//        System.out.println("---QrTools--->params:: "+sb.toString());
        return sb.toString();
    }
}
