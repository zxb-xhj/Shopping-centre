package com.atguigu.gulimall.ware.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: xhj
 * @Date: 2023/04/14/15:19
 * @Description:
 */
@Data
@ToString
public class SkuHasStockVo {
    private Long skuId;

    private Boolean hasStock;
}
