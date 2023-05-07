package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;


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