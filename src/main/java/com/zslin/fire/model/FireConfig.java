package com.zslin.fire.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:fire.properties")
@ConfigurationProperties
@Data
public class FireConfig {

    /**
     * appKey=
     * appSecret=
     * urlTest=http://gateway.visa.2dfire-daily.com
     * urlReady=http://gateway.2dfire-pre.com
     * urlFinal
     */

    private String appKey;

    private String appSecret;

    private String urlTest;

    private String urlReady;

    private String urlFinal;

    private String entityId;
}
