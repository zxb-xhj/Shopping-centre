package com.atguigu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/24/20:29
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category2Vo implements Serializable {
    /**
     * 一级分类id
     */
    private String catalog1Id;
    /**
     * 分类id
     */
    private String id;
    /**
     * 分类名称
     */
    private String name;

    /**
     * 三级分类信息
     */
    private List<Category3Vo> catalog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category3Vo implements Serializable{
        /**
         * 二级分类id
         */
        private String catalog2Id;
        /**
         * 分类id
         */
        private String id;
        /**
         * 分类名称
         */
        private String name;

    }
}
