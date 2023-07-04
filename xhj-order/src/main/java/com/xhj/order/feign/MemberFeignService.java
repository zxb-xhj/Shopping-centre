package com.xhj.order.feign;

import com.xhj.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/03/13:14
 * @Description:
 */
@FeignClient("xhj-member")
public interface MemberFeignService {

    @GetMapping(value = "/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);
}
