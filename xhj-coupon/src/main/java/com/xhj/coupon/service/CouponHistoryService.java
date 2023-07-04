package com.xhj.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com..common.utils.PageUtils;
import com.xhj.coupon.entity.CouponHistoryEntity;

import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:34:51
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

