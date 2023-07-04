package com.xhj.ware.vo;

import lombok.Data;
/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */
@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    private Long itemId;
    private Integer status;
    private String reason;
}
