package com.xhj.order.config;

import com.xhj.order.interceptor.LoginUserIntercaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */

@Configuration
public class OrderWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginUserIntercaptor loginUserIntercaptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserIntercaptor).addPathPatterns("/**");
    }
}
