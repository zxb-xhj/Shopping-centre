package com.xhj.cart.cart.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xhj
 * @Date: 2023/06/20/19:57
 * @Description:
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
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        config.useClusterServers().addNodeAddress() // 集群
        // 根据config配置创建RedisssonClient实例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
