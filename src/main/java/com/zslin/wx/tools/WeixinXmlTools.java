package com.zslin.wx.tools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/23 0:08.
 */
public class WeixinXmlTools {

    /**
     * 生成发送文本消息的XML字符串
     * @param toUser 接收方的openid
     * @param fromUser 开发者微信号
     * @param content 内容
     * @return
     */
    public static String createTextXml(String toUser, String fromUser, String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUser).append("]]></ToUserName>").
                append("<FromUserName><![CDATA[").append(fromUser).append("]]></FromUserName>").
                append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>").
                append("<MsgType><![CDATA[text]]></MsgType>").
                append("<Content><![CDATA[").append(content).append("]]></Content>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 生成发送链接消息的XML字符串
     * @param toUser 接收方的openid
     * @param fromUser 开发者微信号
     * @param content 内容
     * @return
     */
    public static String createUrlXml(String toUser, String fromUser, String title, String url, String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUser).append("]]></ToUserName>").
                append("<FromUserName><![CDATA[").append(fromUser).append("]]></FromUserName>").
                append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>").
                append("<MsgType><![CDATA[link]]></MsgType>").
                append("<Title><![CDATA[").append(title).append("]]></Title>").
                append("<Url><![CDATA[").append(url).append("]]></Url>").
                append("<Description><![CDATA[").append(content).append("]]></Description>");
        sb.append("</xml>");
        return sb.toString();
    }
}
