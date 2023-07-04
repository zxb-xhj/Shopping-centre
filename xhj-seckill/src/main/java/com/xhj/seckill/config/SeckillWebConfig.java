package com.xhj.seckill.config;

import com.xhj.seckill.interceptor.LoginUserIntercaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: xhj
 * @Date: 2023/06/13/16:51
 * @Description:
 */
public class SeckillWebConfig implements WebMvcConfigurer {

    @Autowired
    LoginUserIntercaptor loginUserIntercaptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserIntercaptor).addPathPatterns("/**");
    }
}
