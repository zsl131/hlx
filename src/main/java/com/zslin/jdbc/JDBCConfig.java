package com.zslin.jdbc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:jdbc.properties")
@ConfigurationProperties
@Data
public class JDBCConfig {

    private String dbUrl;

    private String dbname;

    private String password;
}
