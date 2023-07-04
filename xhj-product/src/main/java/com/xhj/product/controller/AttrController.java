package com.xhj.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.xhj.product.entity.ProductAttrValueEntity;
import com.xhj.product.service.ProductAttrValueService;
import com.xhj.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhj.product.service.AttrService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 商品属性
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
@RestController
@Slf4j
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    ///product/attr/update/11
    @PostMapping("/update/{spuId}")
    public R updateSpuId(@PathVariable("spuId") Long spuId,
                         @RequestBody List<ProductAttrValueEntity> productAttrValueEntity){
        productAttrValueService.updateSpuId(spuId,productAttrValueEntity);
        return R.ok();
    }

    //attr/base/listforspu/29
    @RequestMapping("/base/listforspu/{spuId}")
    public R baselistForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> productAttrValue = productAttrValueService.getbaselistForSpu(spuId);
        return R.ok().put("data",productAttrValue);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("{type}/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R baselist(@RequestParam Map<String, Object> params,
                      @PathVariable("type") String type,
                      @PathVariable("catId") Long catId){
        PageUtils page = attrService.queryPageId(params,catId,type);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrVo attr = attrService.getAttrVo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateWrapper(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
