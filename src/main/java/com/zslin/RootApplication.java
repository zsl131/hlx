package com.zslin;

import com.zslin.basic.repository.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/16 11:28.
 */
@EnableJpaRepositories(basePackages = "com.zslin",
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class RootApplication {

    public static void main(String [] args) {
        SpringApplication.run(RootApplication.class, args);
    }
}