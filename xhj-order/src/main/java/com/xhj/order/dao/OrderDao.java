package com.xhj.order.dao;

import com.xhj.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:40:08
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
