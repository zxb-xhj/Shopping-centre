package com.xhj.ware;

import com.xhj.ware.service.WareSkuService;
import com.xhj.ware.service.impl.WareSkuServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class xhjWareApplicationTests{

    @Autowired
    WareSkuService wareSkuService;

    @Autowired
    WareSkuServiceImpl wareSkuServiceImpl;

    @Test
    void contextLoads() {
//        List<Long> arrayList = new ArrayList<>();
//        arrayList.add(1L);
//        arrayList.add(2L);
//        wareSkuService.getSkusHasStock(arrayList);
//        Long aLong = wareSkuServiceImpl.lockSkuStock(29L, 1L, 1);
//        System.out.println(aLong);
    }

}
