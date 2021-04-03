package com.zslin.sms.tools;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:37.
 */
@Component
//@ConfigurationProperties(locations = "classpath:sms-dev.properties")
@PropertySource(value = "classpath:sms-${spring.profiles.active}.properties")
@ConfigurationProperties
@Data
public class SmsConfig {
    private String url;
    private String token;
    private String addModuleCode;
    private String delModuleCode;
    private String listModulesCode;
    private String surplusCode;
    private String sendMsgCode;
    private String sendCodeIid;
}
