package com.xhj.product.vo;

import com.xhj.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/03/25/15:57
 * @Description:
 */
@Data
public class AttrGroupWithattrVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
