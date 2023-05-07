package com.atguigu.gulimall.order;


import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallOrderApplicationTests {

    @Autowired
    OrderService omsOrderService;

    @Test
    void contextLoads() {
        List<OrderEntity> list = omsOrderService.list(new QueryWrapper<OrderEntity>().eq("id", 1L));
        list.forEach((order)->{
            System.out.println(order);
        });

    }

}
