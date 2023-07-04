package com.xhj.order.vo;

import com.xhj.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */

@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /** 错误状态码 **/
    private Integer code;


}
