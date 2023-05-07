package com.atguigu.common.to;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: xhj
 * @Date: 2023/04/14/15:19
 * @Description:
 */
@Data
@ToString
public class SkuHasStockTo {
    private Long skuId;

    private Boolean hasStock;
}
