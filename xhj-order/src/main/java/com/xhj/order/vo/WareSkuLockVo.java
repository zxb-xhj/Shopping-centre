package com.xhj.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */

@Data
public class WareSkuLockVo {

    private String orderSn;

    /** 需要锁住的所有库存信息 **/
    private List<OrderItemVo> locks;



}
