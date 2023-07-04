package com.xhj.order.feign;

import com.xhj.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/03/18:50
 * @Description:
 */
@FeignClient("xhj-cart")
public interface CartFeignService {

    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    List<OrderItemVo> getCurrentCartItems() ;
}
