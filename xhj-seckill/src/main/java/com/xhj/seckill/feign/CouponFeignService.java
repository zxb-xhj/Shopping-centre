package com.xhj.seckill.feign;

import com.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: xhj
 * @Date: 2023/06/12/19:28
 * @Description:
 */
@FeignClient("xhj-coupon")
public interface CouponFeignService {

    @GetMapping(value = "/coupon/seckillsession/Lates3DaySession")
    R getLates3DaySession();
}

