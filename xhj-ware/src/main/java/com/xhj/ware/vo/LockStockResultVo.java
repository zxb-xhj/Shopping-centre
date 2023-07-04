package com.xhj.ware.vo;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */

@Data
public class LockStockResultVo {

    private Long skuId;

    private Integer num;

    /** 是否锁定成功 **/
    private Boolean locked;

}
