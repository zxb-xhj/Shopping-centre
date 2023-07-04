package com.xhj.ware.service;

import com.xhj.ware.vo.FareVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.xhj.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:43:14
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取运费
     * @param addrId
     * @return
     */
    FareVo getFare(Long addrId);
}

