package com.xhj.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/05/13/21:49
 * @Description:
 */

@Data
@ToString
public class SkuItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;

}
