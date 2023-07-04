package com.xhj.product.service.impl;

import com..common.to.es.SkuEsModel;
import com.xhj.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com..common.utils.PageUtils;
import com..common.utils.Query;

import com.xhj.product.dao.ProductAttrValueDao;
import com.xhj.product.entity.ProductAttrValueEntity;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBacthProduct(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> getbaselistForSpu(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return productAttrValueEntities;
    }

    @Override
    public void updateSpuId(Long spuId, List<ProductAttrValueEntity> productAttrValueEntity) {
        //先删除所有
        this.removeById(spuId);
        //在添加
        List<ProductAttrValueEntity> collect = productAttrValueEntity.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> baseAttrListforspu(Long spuId) {
        List<ProductAttrValueEntity> attrValueEntityList = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return attrValueEntityList;
    }

    @Override
    public List<SkuEsModel.Attrs> getByIdList(Long id) {
        List<ProductAttrValueEntity> productAttrValueEntities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", id));
        SkuEsModel skuEsModel = new SkuEsModel();
        List<SkuEsModel.Attrs> attrs = productAttrValueEntities.stream().map(productAttrValueEntity -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            attrs1.setAttrId(productAttrValueEntity.getAttrId());
            attrs1.setAttrName(productAttrValueEntity.getAttrName());
            attrs1.setAttrValue(productAttrValueEntity.getAttrValue());
            return attrs1;
        }).collect(Collectors.toList());
        return attrs;
    }

}