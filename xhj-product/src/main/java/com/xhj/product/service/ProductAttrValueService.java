package com.xhj.product.service;

import com.common.to.es.SkuEsModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.xhj.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBacthProduct(List<ProductAttrValueEntity> collect);

    List<ProductAttrValueEntity> getbaselistForSpu(Long spuId);

    void updateSpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntity);

    List<ProductAttrValueEntity> baseAttrListforspu(Long spuId);

    List<SkuEsModel.Attrs> getByIdList(Long id);
}

