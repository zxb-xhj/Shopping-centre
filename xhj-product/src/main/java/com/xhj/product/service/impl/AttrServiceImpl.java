package com.xhj.product.service.impl;

import com.common.constant.ProductConstant;
import com.xhj.product.dao.AttrAttrgroupRelationDao;
import com.xhj.product.dao.AttrGroupDao;
import com.xhj.product.dao.CategoryDao;
import com.xhj.product.entity.*;
import com.xhj.product.service.CategoryService;
import com.xhj.product.service.SpuInfoService;
import com.xhj.product.vo.AttrRespVo;
import com.xhj.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xhj.product.service.AttrService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.xhj.product.dao.AttrDao;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
@Slf4j
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SpuInfoService spuInfoService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);

        //关联关系
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrgroupRelation = new AttrAttrgroupRelationEntity();
            attrgroupRelation.setAttrGroupId(attr.getAttrGroupId());
            attrgroupRelation.setAttrId(attrEntity.getAttrId());
            attrgroupRelationDao.insert(attrgroupRelation);
        }

    }

    @Transactional
    @Override
    public PageUtils queryPageId(Map<String, Object> params, Long id, String type) {

        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type", "base".equalsIgnoreCase(type) ?
                        ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        String key = (String) params.get("key");
        if (id != 0) {
            wrapper.eq("catelog_id", id);
        } else if (!StringUtils.isEmpty(key)) {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);


        //设置分类和分组
        List<AttrEntity> records = page.getRecords();
        Stream<AttrRespVo> voStream = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            log.info("attrEntity==="+attrEntity);
            log.info("attrRespVo==="+attrRespVo);
            if (attrEntity.getAttrType() == 1) {
            AttrAttrgroupRelationEntity attrgroupRelation = attrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrEntity.getAttrId()));
            log.info("attrgroupRelation==="+attrgroupRelation );
            if(attrgroupRelation != null){
                AttrGroupEntity attrGroup = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
                if (attrGroup != null) {
                    attrRespVo.setGroupName(attrGroup.getAttrGroupName());
                }
            }
            }
            CategoryEntity category = categoryDao.selectById(attrEntity.getCatelogId());
            if (category != null) {
                attrRespVo.setCatelogName(category.getName());
            }
            log.info("attrRespVo===" + attrRespVo);
            return attrRespVo;
        });
        List<AttrRespVo> collect = voStream.collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Transactional
    @Override
    public AttrVo getAttrVo(Long attrId) {
        AttrRespVo attrVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrVo);

        AttrAttrgroupRelationEntity attrgroupRelation = attrgroupRelationDao.selectOne(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
        if (attrgroupRelation != null) {
            attrVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
            AttrGroupEntity attrGroup = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
            if (attrGroup != null) {
                attrVo.setGroupName(attrGroup.getAttrGroupName());
            }
        }
        ArrayList<Long> longArrayList = new ArrayList<Long>();
        List<Long> catelogIds = categoryService.getListCatelogIds(attrVo.getCatelogId());
        Long[] longs = catelogIds.stream().toArray(Long[]::new);
        attrVo.setCatelogPath(longs);
        CategoryEntity category = categoryDao.selectById(attrVo.getCatelogId());
        if (category != null) {
            attrVo.setCatelogName(category.getName());
        }
        return attrVo;
    }

    @Override
    public void updateWrapper(AttrVo attrvo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrvo, attrEntity);
        log.info("attrEntity: " + attrEntity);
        this.updateById(attrEntity);
        if (attrEntity != null) {
            UpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("attr_id", attrEntity.getAttrId()).set("attr_group_id", attrvo.getAttrGroupId());
            attrgroupRelationDao.update(null, updateWrapper);
        }
    }

    @Transactional
    @Override
    public List<AttrEntity> getAttrRelation(Long attrGroupId) {
        QueryWrapper<AttrAttrgroupRelationEntity> relationEntityQueryWrapper = new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> attrgroupRelationEntities = attrgroupRelationDao.selectList(relationEntityQueryWrapper);
        List<Long> attrIds = attrgroupRelationEntities.stream().map((attrgroupRelation) -> {
            return attrgroupRelation.getAttrId();
        }).collect(Collectors.toList());
        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Transactional
    @Override
    public PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId) {
        //当前分组只能关联自己所属的分类里面的属性
        AttrGroupEntity attrGroup = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1、当前分类下的其他分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        List<Long> collect = attrGroupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.3、从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        //2.2、这些分组关联的属性
        if (collect != null && collect.size()>0){
            List<AttrAttrgroupRelationEntity> relationEntities = attrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .in("attr_group_id",collect));
            List<Long> attrIds = relationEntities.stream().map((item) -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            if (attrIds != null && attrIds.size()>0){
                wrapper.notIn("attr_id", attrIds);
            }
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
//        List<AttrEntity> attrEntities = this.baseMapper.selectList();
        IPage<AttrEntity> entityIPage = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(entityIPage);
        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        List<Long> attrIdList = baseMapper.selectList(new QueryWrapper<AttrEntity>()
                        .in("attr_id", attrIds).eq("search_type", 1))
                .stream().map(attrEntity -> {
                    return attrEntity.getAttrId();
                }).collect(Collectors.toList());
        return attrIdList;
    }


}