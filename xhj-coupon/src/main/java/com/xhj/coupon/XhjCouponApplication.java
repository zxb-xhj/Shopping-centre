package com.xhj.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan("com.xhj.coupon.dao")
public class XhjCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjCouponApplication.class, args);
    }

}
