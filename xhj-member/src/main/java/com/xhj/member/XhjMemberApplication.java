package com.xhj.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com..xhj.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com..xhj.member.dao")
public class XhjMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjMemberApplication.class, args);
    }

}
