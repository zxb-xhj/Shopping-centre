package com.xhj.order.to;

import com.xhj.order.entity.OrderEntity;
import com.xhj.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/03/18:50
 * @Description:
 */

@Data
public class OrderCreateTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
