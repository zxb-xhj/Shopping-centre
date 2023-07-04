package com.xhj.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 秒杀活动场次
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:34:50
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 场次名称
	 */
	private String name;
	/**
	 * 每日开始时间
	 */
	private String startTime;
	/**
	 * 每日结束时间
	 */
	private String endTime;
	/**
	 * 启用状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private String createTime;

	@TableField(exist = false)
	private List<SeckillSkuRelationEntity> relationSkus;

}
