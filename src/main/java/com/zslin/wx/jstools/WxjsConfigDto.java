package com.zslin.wx.jstools;

import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/24 15:11.
 * 微信Js配置DTO对象
 */
public class WxjsConfigDto {

    private String appid;

    private String timestamp;

    private String nonceStr;

    private String signature;

    private String ticket;

    @Override
    public String toString() {
        return "WxjsConfigDto{" +
                "appid='" + appid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", signature='" + signature + '\'' +
                ", ticket='" + ticket + '\'' +
                '}';
    }

    public WxjsConfigDto(String appid, String timestamp, String nonceStr, String signature, String ticket) {
        this.appid = appid;
        this.timestamp = timestamp;
        this.nonceStr = nonceStr;
        this.signature = signature;
        this.ticket = ticket;
    }

    public WxjsConfigDto(String appid, Map<String, String> config) {
        this.appid = appid;
        this.timestamp = config.get("timestamp");
        this.nonceStr = config.get("nonceStr");
        this.signature = config.get("signature");
        this.ticket = config.get("jsapi_ticket");
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
