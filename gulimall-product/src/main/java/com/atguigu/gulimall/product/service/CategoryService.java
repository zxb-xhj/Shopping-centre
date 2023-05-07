package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.Category2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    List<Long> getListCatelogIds(Long catelogId);

    void updateCategoryBrandRelation(CategoryEntity category);

    void updateBatchByIdCategoryBrandRelation(CategoryEntity[] category);

    List<CategoryEntity> getListOneCategory();

    Map<String, List<Category2Vo>> getListCategoryJson();

    List<CategoryEntity> getLeve1Categorys();
}

