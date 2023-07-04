package com.xhj.product.service;

import com.xhj.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com..common.utils.PageUtils;
import com.xhj.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryPageId(Map<String, Object> params, Long id, String type);

    AttrVo getAttrVo(Long attrId);

    void updateWrapper(AttrVo attr);

    List<AttrEntity> getAttrRelation(Long attrGroupId);

    PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

