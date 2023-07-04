package com.xhj.seckill.service;

import com.xhj.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/12/19:24
 * @Description:
 */
public interface SeckillService {
    /**
     * 上架三天需要秒杀的商品
     */
    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    SeckillSkuRedisTo getSkuSeckilInfo(Long skuId);

    String kill(String killId, String key, Integer num);

//    /**
//     * 当前商品进行秒杀（秒杀开始）
//     * @param killId
//     * @param key
//     * @param num
//     * @return
//     */
//    String kill(String killId, String key, Integer num) throws InterruptedException;
}
