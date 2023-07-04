package com.xhj.product.service.impl;

import com.xhj.product.vo.AttrAttrgroupRelationVo;
import com.xhj.product.service.AttrAttrgroupRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com..common.utils.PageUtils;
import com..common.utils.Query;

import com.xhj.product.dao.AttrAttrgroupRelationDao;
import com.xhj.product.entity.AttrAttrgroupRelationEntity;
import org.springframework.transaction.annotation.Transactional;


@Service("attrAttrgroupRelationService")
@Slf4j
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    AttrAttrgroupRelationDao attrgroupRelationDao;

    @Autowired
    AttrAttrgroupRelationService attrgroupRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void deleteBatchelation(List<AttrAttrgroupRelationVo> relationVo) {
        relationVo.forEach((attrgroupRelationVo)->{
            QueryWrapper<AttrAttrgroupRelationEntity> entityQueryWrapper = new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrgroupRelationVo.getAttrId())
                    .eq("attr_group_id", attrgroupRelationVo.getAttrGroupId());
            AttrAttrgroupRelationEntity attrgroupRelation = attrgroupRelationDao.selectOne(entityQueryWrapper);
            log.info("attrgroupRelation=="+attrgroupRelation);
            attrgroupRelationDao.delete(entityQueryWrapper);
        });
//        attrgroupRelationDao.deleteBatchelation(entities);
    }

    @Override
    public void saveBatchVo(List<AttrAttrgroupRelationVo> relationVos) {
        List<AttrAttrgroupRelationEntity> collect = relationVos.stream().map((relationVo) -> {
            AttrAttrgroupRelationEntity attrgroupRelation = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(relationVo, attrgroupRelation);
            return attrgroupRelation;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}