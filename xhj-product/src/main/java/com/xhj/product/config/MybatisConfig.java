package com.xhj.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: xhj
 * @Date: 2023/03/18/13:34
 * @Description:
 */
@Configuration
@MapperScan("com.xhj.product.dao")
@EnableTransactionManagement //开启事务
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //请求页面大于最大页，从1开始
        paginationInterceptor.setOverflow(true);
        //最大单页数量，默认500，-1不受限制
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }

}
