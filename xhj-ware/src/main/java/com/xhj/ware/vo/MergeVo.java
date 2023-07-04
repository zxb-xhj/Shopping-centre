package com.xhj.ware.vo;

import lombok.Data;

import java.util.List;
/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */
@Data
public class MergeVo {

   private Long purchaseId; //整单id
   private List<Long> items;//[1,2,3,4] //合并项集合
}
