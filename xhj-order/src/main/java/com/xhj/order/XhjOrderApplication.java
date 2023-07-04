package com.xhj.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 导入spring-boot-starter-aop 使用aspectj
 * @EnableAspectJAutoProxy(exposeProxy = true) 开启aspectj动态代理功能，以后所有动态代理都是aspectj创建的（即使没有接口也可以创建）
 *   对外暴露
 *   用代理对象调用
 *   OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();
 *         orderService.b();
 *         orderService.c();
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRedisHttpSession
@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients("com.xhj.order.feign")
@MapperScan("com.xhj.order.dao")
public class XhjOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhjOrderApplication.class, args);
    }

}
