package com.xhj.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com..xhj.ware.dao")
@EnableDiscoveryClient
@EnableFeignClients("com..xhj.ware.feign")
@EnableTransactionManagement
public class XhjWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjWareApplication.class, args);
    }

}
