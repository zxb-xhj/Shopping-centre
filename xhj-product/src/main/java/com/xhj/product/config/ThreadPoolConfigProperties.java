package com.xhj.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: xhj
 * @Date: 2023/05/14/13:45
 * @Description:
 */
@ConfigurationProperties(prefix = "xhj.thread")
// @Component
@Data
public class ThreadPoolConfigProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;


}
