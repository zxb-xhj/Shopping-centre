package com.xhj.product.service.impl;

import com.xhj.product.dao.BrandDao;
import com.xhj.product.dao.CategoryDao;
import com.xhj.product.entity.BrandEntity;
import com.xhj.product.entity.CategoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.xhj.product.dao.CategoryBrandRelationDao;
import com.xhj.product.entity.CategoryBrandRelationEntity;
import com.xhj.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
@Slf4j
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }



    @Override
    public List<CategoryBrandRelationEntity> getattrById(Long id,String brandName) {
        QueryWrapper<CategoryBrandRelationEntity> categoryBrandRelationEntityQueryWrapper = new QueryWrapper<>();
        categoryBrandRelationEntityQueryWrapper.eq("brand_id",id).or().eq("brand_name",brandName);
        List<CategoryBrandRelationEntity> list = this.list(categoryBrandRelationEntityQueryWrapper);
        return list;
    }

    /**
     * 获取属性列表名称
     * @param
     * @return
     */
    @Override
    public String getBrandName(CategoryBrandRelationEntity categoryBrandRelation) {
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        return brandEntity.getName();
    }

    /**
     * 获取品牌名称
     * @param
     * @return
     */
    @Override
    public String getCatelogName(CategoryBrandRelationEntity categoryBrandRelation) {
        CategoryEntity category = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        return category.getName();
    }

    /**
     * 根据catId获取品牌信息
     * @param catId
     * @return
     */
    @Override
    public List<CategoryBrandRelationEntity> relationBransList(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> queryWrapper = new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id", catId);
        List<CategoryBrandRelationEntity> brandRelationEntities = this.list(queryWrapper);
        log.info("brandRelationEntities==="+brandRelationEntities);
        return brandRelationEntities;
    }

}