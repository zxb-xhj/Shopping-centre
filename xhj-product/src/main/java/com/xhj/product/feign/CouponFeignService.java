package com.xhj.product.feign;

import com..common.to.SkuReductionTo;
import com..common.to.SpuBoundTo;
import com..common.utils.R;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: xhj
 * @Date: 2023/03/26/14:20
 * @Description:
 */
@FeignClient("xhj-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
