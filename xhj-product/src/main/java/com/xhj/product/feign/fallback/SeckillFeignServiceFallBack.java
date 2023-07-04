package com.xhj.product.feign.fallback;

import com.common.exception.BizCodeEnum;
import com.common.utils.R;
import com.xhj.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: xhj
 * @Date: 2023/04/15/13:20
 * @Description:
 */
@Component
@Slf4j
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        log.info("熔断方法调用...getSkuSeckilInfo");
        return R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(),BizCodeEnum.TO_MANY_REQUEST.getMsg());
    }
}
