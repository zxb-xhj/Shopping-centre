package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: xhj
 * @Date: 2023/02/09/23:02
 * @Description:
 */
@FeignClient("gulimall-coupon")
public interface CouponfeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberlist();

}
