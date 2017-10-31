package com.czy.seed.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by PLC on 2017/5/21.
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class})
@ImportResource("spring/applicationContext.xml")
public class Start {
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);

    }

}
