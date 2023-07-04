package com..common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: xhj
 * @Date: 2023/03/14/15:26
 * @Description:
 */

public class NoStockException extends RuntimeException {

    @Getter @Setter
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品id："+ skuId + "库存不足！");
    }

    public NoStockException(String msg) {
        super(msg);
    }


}
