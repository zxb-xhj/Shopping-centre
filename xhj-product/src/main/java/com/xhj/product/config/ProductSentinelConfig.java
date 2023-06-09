package com.xhj.product.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.common.exception.BizCodeEnum;
import com.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xhj
 * @Date: 2023/05/30/18:38
 * @Description:
 */
@Configuration
public class ProductSentinelConfig {

    public ProductSentinelConfig(){
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler(){
            @Override
            public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
                R error = R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(), BizCodeEnum.TO_MANY_REQUEST.getMsg());
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(JSON.toJSONString(error));
            }
        });
    }
}
