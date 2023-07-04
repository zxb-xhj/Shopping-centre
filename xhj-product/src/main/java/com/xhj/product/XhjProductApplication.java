package com.xhj.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* @Author : xhj
* @Date: 2023/2/7 16:30
 *
 * 1.导入mybatis-plus包 mysql驱动
 * 2、创建application.yml
 *     （1）配置数据库信息
 *     （2）配置mybatis-plus
*/
@EnableRabbit
@EnableRedisHttpSession
@MapperScan("com..xhj.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients("com..xhj.product.feign")
public class XhjProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjProductApplication.class, args);
    }

}
