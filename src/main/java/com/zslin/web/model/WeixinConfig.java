package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/18 16:53.
 * 微信配置
 */
@Entity
@Table(name = "t_weixin_config")
public class WeixinConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //链接地址，即主域名，如：http://zsl8.5166.info
    private String url;
    private String appid;
    private String secret;
    private String token;
    private String aeskey;
    private String eventTemp;
    /** 商户ID，微信支付时申请的 */
    private String mchid;

    /** 微信支付结果通知地址 */
    @Column(name = "notify_url")
    private String notifyUrl;

    /** APIKEY，微信支付时所需要的APIKEY */
    @Column(name = "api_key")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventTemp() {
        return eventTemp;
    }

    public void setEventTemp(String eventTemp) {
        this.eventTemp = eventTemp;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
