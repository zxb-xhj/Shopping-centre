package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xhj
 * @Date: 2023/04/27/18:07
 * @Description: RedissonClient 配置
 */
@Configuration
public class MyRedissonConfig {

    /**
     * 所有redisson的使用都是通过RedissonClient对象
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        // 创建配置
        Config config = new Config();
        // 添加redis地址
        config.useSingleServer().setAddress("redis://47.120.6.55:6379");
        // 根据config配置创建RedisssonClient实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
