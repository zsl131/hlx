package com.zslin.wx.tools;

import com.zslin.web.model.Article;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 构建文章推送数据
     * @param toUser 接收用户openid
     * @param fromUser 开发者
     * @param article 文章
     * @param baseUrl 基础链接地址
     * @return
     */
    public static String buildArticleStr(String toUser, String fromUser, Article article, String baseUrl) {
        List<Article> list = new ArrayList<>();
        list.add(article);
        return buildSubscribeStr(toUser, fromUser, list, baseUrl);
    }

    /**
     * 构建关注时的数据
     * @param toUser 接收用户Openid
     * @param fromUser 发送方
     * @param articleList 文章列表
     * @param baseUrl 文章链接基础地址
     * @return
     */
    public static String buildSubscribeStr(String toUser, String fromUser, List<Article> articleList, String baseUrl) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUser).append("]]></ToUserName>").
                append("<FromUserName><![CDATA[").append(fromUser).append("]]></FromUserName>").
                append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>").
                append("<MsgType><![CDATA[news]]></MsgType>").
                append("<ArticleCount>"+articleList.size()+"</ArticleCount>"). //TODO 这里设置文章条数
                append("<Articles>");

        for(Article dto : articleList) {
            sb.append("<item>").
                    append("<Title><![CDATA[").append(dto.getTitle()).append("]]></Title>").
                    append("<Description><![CDATA[").append(dto.getGuide()).append("]]></Description>").
                    append("<PicUrl><![CDATA[").append(baseUrl+dto.getPicPath()).append("]]></PicUrl>").
                    append("<Url><![CDATA[").append(baseUrl+"/weixin/article/detail?id="+dto.getId()).append("]]></Url>").
                    append("</item>");
        }
        sb.append("</Articles>");
        sb.append("</xml>");
        return sb.toString();
    }
}
