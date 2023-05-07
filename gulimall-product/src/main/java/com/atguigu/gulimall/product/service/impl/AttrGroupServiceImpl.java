package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.AttrGroupWithattrVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, long catId) {
        if (catId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>()
            );
            return new PageUtils(page);
        }else{
            String key = (String) params.get("key");

            QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catId);

            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper
            );
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupEntity> queryId(long catId) {

        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);
        List<AttrGroupEntity> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<AttrGroupWithattrVo> getAttrgroupWithattrsByCatelogIds(Long catelogId) {
        //查询分组信息
        List<AttrGroupEntity> attrGroupEntityList = this.list(
                new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //查询所有属性
        List<AttrGroupWithattrVo> collect = attrGroupEntityList.stream().map(attrGroupEntity -> {
            AttrGroupWithattrVo attrGroupWithattrVo = new AttrGroupWithattrVo();
            BeanUtils.copyProperties(attrGroupEntity,attrGroupWithattrVo);
            List<AttrEntity> attrRelations = attrService.getAttrRelation(attrGroupEntity.getAttrGroupId());
            if (CollectionUtils.isNotEmpty(attrRelations)){
                attrGroupWithattrVo.setAttrs(attrRelations);
            } else {
                attrGroupWithattrVo.setAttrs(new ArrayList<>());
            }
            return attrGroupWithattrVo;
        }).collect(Collectors.toList());

        return collect;
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>();
        queryWrapper.and((obj)->{
           obj.eq("attr_group_id",params.get("key")).or().like("attr_group_name",params.get("key"));
        });

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}