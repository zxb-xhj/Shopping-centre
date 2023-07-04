package com.xhj.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;
}
