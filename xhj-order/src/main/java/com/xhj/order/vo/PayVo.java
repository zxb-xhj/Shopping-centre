package com.xhj.order.vo;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */
@Data
public class PayVo {

    private String out_trade_no; // 商户订单号 必填
    private String subject; // 订单名称 必填
    private String total_amount;  // 付款金额 必填
    private String body; // 商品描述 可空
}
