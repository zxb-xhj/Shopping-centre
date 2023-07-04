package com.xhj.product.service;

import com.xhj.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com..common.utils.PageUtils;
import com.xhj.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageCondition(Map<String, Object> params);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

