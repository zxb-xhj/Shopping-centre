package com.xhj.auth.feign;

import com.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xhj
 * @Date: 2023/05/14/17:23
 * @Description:
 */
@FeignClient("xhj-third-party")
public interface ThirdPartFeignService {

    @GetMapping("/sms/smscode")
    public R smsCoe(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
