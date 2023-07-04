package com.xhj.coupon;

import com.xhj.coupon.entity.CouponHistoryEntity;
import com.xhj.coupon.service.CouponHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class XhjCouponApplicationTests {

    @Autowired
    CouponHistoryService couponHistoryService;

    @Test
    void contextLoads() {
        List<CouponHistoryEntity> list = couponHistoryService.list();
        list.forEach((coupon)->{
            System.out.println(coupon);
        });

    }

}
