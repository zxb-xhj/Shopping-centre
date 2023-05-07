package com.atguigu.gulimall.ware;

import com.atguigu.gulimall.ware.service.WareSkuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GulimallWareApplicationTests{

    @Autowired
    WareSkuService wareSkuService;

    @Test
    void contextLoads() {
        List<Long> arrayList = new ArrayList<>();
        arrayList.add(1L);
        arrayList.add(2L);
        wareSkuService.getSkusHasStock(arrayList);
    }

}
