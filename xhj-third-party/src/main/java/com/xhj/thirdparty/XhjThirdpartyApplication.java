package com.xhj.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: xhj
 * @Date: 2023/03/09/14:43
 * @Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class XhjThirdpartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjThirdpartyApplication.class, args);
    }

}