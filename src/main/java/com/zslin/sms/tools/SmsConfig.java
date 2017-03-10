package com.zslin.sms.tools;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:37.
 */
@Component
@ConfigurationProperties(locations = "classpath:sms.properties")
public class SmsConfig {
    private String url;
    private String token;
    private String addModuleCode;
    private String delModuleCode;
    private String listModulesCode;
    private String surplusCode;
    private String sendMsgCode;
    private String sendCodeIid;

    public String getSendCodeIid() {
        return sendCodeIid;
    }

    public void setSendCodeIid(String sendCodeIid) {
        this.sendCodeIid = sendCodeIid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddModuleCode() {
        return addModuleCode;
    }

    public void setAddModuleCode(String addModuleCode) {
        this.addModuleCode = addModuleCode;
    }

    public String getDelModuleCode() {
        return delModuleCode;
    }

    public void setDelModuleCode(String delModuleCode) {
        this.delModuleCode = delModuleCode;
    }

    public String getListModulesCode() {
        return listModulesCode;
    }

    public void setListModulesCode(String listModulesCode) {
        this.listModulesCode = listModulesCode;
    }

    public String getSurplusCode() {
        return surplusCode;
    }

    public void setSurplusCode(String surplusCode) {
        this.surplusCode = surplusCode;
    }

    public String getSendMsgCode() {
        return sendMsgCode;
    }

    public void setSendMsgCode(String sendMsgCode) {
        this.sendMsgCode = sendMsgCode;
    }
}
