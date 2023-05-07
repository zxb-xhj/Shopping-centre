package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.vo.CategoryBrandRelationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
@RestController
@RequestMapping("product/categorybrandrelation")
@Slf4j
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * categorybrandrelation/brands/list
     * 根据 catId 查询对应的 品牌
     * @return
     */
    @GetMapping("/brands/list")
    public R relationBransList(@RequestParam("catId") Long catId){
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationService.relationBransList(catId);

        List<CategoryBrandRelationVo> collect = categoryBrandRelationEntities.stream().map(categoryBrandRelationEntity -> {
            CategoryBrandRelationVo categoryBrandRelationVo = new CategoryBrandRelationVo();
            categoryBrandRelationVo.setBrandId(categoryBrandRelationEntity.getBrandId());
            categoryBrandRelationVo.setBrandName(categoryBrandRelationEntity.getBrandName());
            return categoryBrandRelationVo;
        }).collect(Collectors.toList());
        log.info("collect==="+collect);
        return R.ok().put("data",collect);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/catelog/list")
    //@RequiresPermissions("product:attrattrgrouprelation:info")
    public R infobrandId(@RequestParam("brandId") Long brandId,
                         @RequestParam("brandName") String brandName){
        List<CategoryBrandRelationEntity> CategoryBrandRelationEntity = categoryBrandRelationService.getattrById(brandId,brandName);
        log.info("attr==="+CategoryBrandRelationEntity);
        return R.ok().put("data", CategoryBrandRelationEntity);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        log.info("save ==== "+categoryBrandRelation);
        categoryBrandRelation.setBrandName(categoryBrandRelationService.getBrandName(categoryBrandRelation));
        categoryBrandRelation.setCatelogName(categoryBrandRelationService.getCatelogName(categoryBrandRelation));
		categoryBrandRelationService.save(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
