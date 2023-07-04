package com.xhj.coupon.entity.vo;

import com.xhj.coupon.entity.SeckillSkuRelationEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/06/13/11:00
 * @Description:
 */
@Data
public class SeckillSessionEntityVo {

    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    private List<SeckillSkuRelationEntity> relationSkus;
}
