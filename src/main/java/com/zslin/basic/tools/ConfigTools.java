package com.zslin.basic.tools;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by zsl-pc on 2016/9/11.
 */
@Configuration
@Component
@Data
public class ConfigTools { // extends WebMvcConfigurerAdapter

    @Value("${config.filePath}")
    private String filePath;

    @Value("${spring.application.name}")
    private String appName;

    public String getFilePath() {
        return getFilePath("");
    }

    public String getFilePath(String basePath) {
        File f = new File(filePath+basePath);
        if(!f.exists()) {f.mkdirs();}
        return f.getAbsolutePath()+File.separator;
    }
}
