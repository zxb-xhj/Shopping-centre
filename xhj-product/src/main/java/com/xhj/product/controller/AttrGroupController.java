package com.xhj.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.xhj.product.entity.AttrEntity;
import com.xhj.product.service.AttrAttrgroupRelationService;
import com.xhj.product.service.AttrService;
import com.xhj.product.service.CategoryService;
import com.xhj.product.vo.AttrAttrgroupRelationVo;
import com.xhj.product.vo.AttrGroupWithattrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhj.product.entity.AttrGroupEntity;
import com.xhj.product.service.AttrGroupService;
import com..common.utils.PageUtils;
import com..common.utils.R;


/**
 * 属性分组
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
@RestController
@Slf4j
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrgroupRelationService;

    //product/attrgroup/225/withattr
    @GetMapping("/{catelogId}/withattr")
    public R attrgroupWithattrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithattrVo> groupWithattrVos = attrGroupService.getAttrgroupWithattrsByCatelogIds(catelogId);
        return R.ok().put("data",groupWithattrVos);
    }

    //product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R AttrRelation(@RequestBody List<AttrAttrgroupRelationVo> relationVos){
        attrgroupRelationService.saveBatchVo(relationVos);
        return R.ok();
    }

    ///product/attrgroup/attr/relation/delete
    /**
     * 批量删除
     * @param relationVo
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R attrRelationDelete(@RequestBody List<AttrAttrgroupRelationVo> relationVo){
        log.info("AttrAttrgroupRelationVo==="+relationVo);
        attrgroupRelationService.deleteBatchelation(relationVo);
        return R.ok();
    }

    //product/attrgroup/1/noattr/relation
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R getNoAttrRelation(@PathVariable("attrGroupId") Long attrGroupId,
                               @RequestParam Map<String, Object> params){
        PageUtils pageUtils = attrService.getNoAttrRelation(params,attrGroupId);
        return R.ok().put("page",pageUtils);
    }

    //"/product/attrgroup/" + this.attrGroupId + "/attr/relation"
    /**
     * 属性
     * @param attrGroupId
     * @return
     */
    @GetMapping("{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> entities = attrService.getAttrRelation(attrGroupId);
        return R.ok().put("data",entities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catId") long catId) {
//        PageUtils page = attrGroupService.queryPage(params);

        PageUtils pageUtils = attrGroupService.queryPage(params, catId);

        return R.ok().put("page", pageUtils);
    }

    /**
     * 列表
     */
    @RequestMapping("/listId/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R listId(@PathVariable("catId") long catId) {
//        PageUtils page = attrGroupService.queryPage(params);

        List<AttrGroupEntity> attrGroup = attrGroupService.queryId(catId);
//        log.info("list+++++====" + attrGroup);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 列表id
     */
    @RequestMapping("/Idlist")
    //@RequiresPermissions("product:attrgroup:list")
    public R idlist(@RequestParam Map<String, Object> params) {
//        PageUtils page = attrGroupService.queryPage(params);

        PageUtils pageUtils = attrGroupService.queryPage(params);

        return R.ok().put("page", pageUtils);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        attrGroup.setCatelogIds(categoryService.getListCatelogIds(attrGroup.getCatelogId()));

//        log.info("attrGroup" + attrGroup);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);
//        log.info("====" + attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
