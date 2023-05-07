package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import feign.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> getattrById(@Param("brandId") Long id, @Param("brandName") String brandName);

    String getBrandName(CategoryBrandRelationEntity categoryBrandRelation);

    String getCatelogName(CategoryBrandRelationEntity categoryBrandRelation);

    List<CategoryBrandRelationEntity> relationBransList(Long catId);
}

