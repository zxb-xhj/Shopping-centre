package com.xhj.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com..common.utils.PageUtils;
import com.xhj.coupon.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:34:50
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

