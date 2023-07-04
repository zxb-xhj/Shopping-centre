package com.xhj.product.dao;

import com.xhj.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-07 15:45:10
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
