package com.common.to.mq;

import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/03/26/14:29
 * @Description:
 */

@Data
public class StockLockedTo {

    /** 库存工作单的id **/
    private Long id;

    /** 工作单详情的所有信息 **/
    private StockDetailTo detailTo;
}
