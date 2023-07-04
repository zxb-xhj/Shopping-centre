package com.xhj.cart.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Author: xhj
 * @Date: 2023/05/17/10:20
 * @Description:
 */

@Configuration
public class XhjSessionConfig {

    // 配置分布式session，扩大session
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("xhj.com");
        serializer.setCookieName("GULISESSION");
        return serializer;
    }

    /**
     * 设置用redis序列化
     * @return
     */
    @Bean
    public RedisSerializer<Object> redisSerializer(){
        return new JdkSerializationRedisSerializer();
    }

}
