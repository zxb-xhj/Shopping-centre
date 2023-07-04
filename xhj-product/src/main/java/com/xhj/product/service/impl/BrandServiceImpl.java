package com.xhj.product.service.impl;

import com.xhj.product.dao.CategoryBrandRelationDao;
import com.xhj.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com..common.utils.PageUtils;
import com..common.utils.Query;

import com.xhj.product.dao.BrandDao;
import com.xhj.product.entity.BrandEntity;
import com.xhj.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<BrandEntity>().eq("brand_id", key).or().eq("name", key);
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    queryWrapper
            );
            return new PageUtils(page);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateCategoryBrandRelation(BrandEntity brand) {
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("brand_name",brand.getName()).eq("brand_id",brand.getBrandId());
        categoryBrandRelationDao.update(null,updateWrapper);
    }

}