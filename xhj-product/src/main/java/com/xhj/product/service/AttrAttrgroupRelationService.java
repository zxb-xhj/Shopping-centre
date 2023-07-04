package com.xhj.product.service;

import com.xhj.product.vo.AttrAttrgroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.xhj.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteBatchelation(List<AttrAttrgroupRelationVo> relationVo);

    void saveBatchVo(List<AttrAttrgroupRelationVo> relationVos);
}

